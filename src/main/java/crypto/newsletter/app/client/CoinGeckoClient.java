package crypto.newsletter.app.client;

import crypto.newsletter.app.client.contract.AssetPrice;
import feign.FeignException;
import org.springframework.cloud.openfeign.CollectionFormat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "coin-gecko")
public interface CoinGeckoClient {

    @GetMapping("/simple/price")
    @CollectionFormat(feign.CollectionFormat.CSV)
    @Retryable(retryFor = FeignException.class, maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2))
    Map<String, AssetPrice> fetchUsdCourses(@RequestParam Set<String> ids,
                                            @RequestParam("vs_currencies") String quoteCurrency,
                                            @RequestParam String precision);
}
