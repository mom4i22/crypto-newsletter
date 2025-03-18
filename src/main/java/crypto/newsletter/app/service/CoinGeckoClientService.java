package crypto.newsletter.app.service;

import crypto.newsletter.app.client.CoinGeckoClient;
import crypto.newsletter.app.client.contract.AssetPrice;
import crypto.newsletter.app.model.CryptocurrencyEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoinGeckoClientService {

    private static final String QUOTE_CURRENCY = "usd";
    private static final String PRICE_PRECISION = "8";

    private final CoinGeckoClient coinGeckoClient;
    private final CryptocurrencyService cryptocurrencyService;

    public Map<String, BigDecimal> fetchPrices() {
        Set<String> coinGeckoIds = new HashSet<>();
        Map<String, String> currencyNameToCoinGeckoIdMapping = cryptocurrencyService.getCryptocurrencies()
                .peek(event -> coinGeckoIds.add(event.getCoinGeckoId()))
                .collect(Collectors.toMap(
                        CryptocurrencyEvent::getCoinGeckoId,
                        CryptocurrencyEvent::getName
                ));

        log.info("Fetching usd courses from CoinGecko for {} assets {}", coinGeckoIds.size(), coinGeckoIds);
        return fetchPrices(currencyNameToCoinGeckoIdMapping, coinGeckoIds);
    }

    private Map<String, BigDecimal> fetchPrices(Map<String, String> currencyNameToCoinGeckoIdMapping,
                                                Set<String> coinGeckoIds) {
        Map<String, AssetPrice> pricesMap;
        try {
            pricesMap = coinGeckoClient.fetchUsdCourses(
                    coinGeckoIds,
                    QUOTE_CURRENCY,
                    PRICE_PRECISION
            );
        } catch (Exception e) {
            log.error("Error fetching usd courses from CoinGecko for assets {}", coinGeckoIds);
            throw new UnableToFetchUsdCoursesException("Could not fetch usd courses", e);
        }

        return pricesMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> currencyNameToCoinGeckoIdMapping.get(entry.getKey()),
                        entry -> entry.getValue().price()
                ));
    }

    @StandardException
    static class UnableToFetchUsdCoursesException extends RuntimeException {
    }
}
