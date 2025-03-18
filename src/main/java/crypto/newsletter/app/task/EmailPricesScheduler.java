package crypto.newsletter.app.task;

import crypto.newsletter.app.service.CoinGeckoClientService;
import crypto.newsletter.app.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailPricesScheduler {

    private final EmailService emailService;
    private final CoinGeckoClientService coinGeckoClientService;

    @Scheduled(cron = "0 * * * * *")
    void fetchAndEmailPrices() {
        log.info("Starting scheduled emailing of crypto prices");

        try {
            Map<String, BigDecimal> cryptoPrices = coinGeckoClientService.fetchPrices();
            emailService.sendEmails(cryptoPrices);
        } catch (Exception e) {
            log.error("Failed to fetch and email crypto prices", e);
        }

        log.info("Finished scheduled emailing of crypto prices");
    }
}
