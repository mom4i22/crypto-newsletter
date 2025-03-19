package crypto.newsletter.app.service;

import crypto.newsletter.app.exception.EmailNotFoundException;
import crypto.newsletter.app.model.EmailEvent;
import crypto.newsletter.app.producer.EmailProducer;
import crypto.newsletter.app.repository.EmailStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

@Slf4j
@Service
public class EmailService {

    private static final String VERIFIED_EMAIL_KEY = "verified";
    private static final String UNVERIFIED_EMAIL_KEY = "unverified";

    private final EmailStore emailStore;
    private final EmailProducer emailProducer;
    private final Duration mailVerificationWindow;
    private final EmailProcessorService emailProcessorService;

    public EmailService(EmailStore emailStore,
                        EmailProducer emailProducer,
                        EmailProcessorService emailProcessorService,
                        @Value("${mail.verification.window}") Duration mailVerificationWindow) {
        this.emailProducer = emailProducer;
        this.emailStore = emailStore;
        this.emailProcessorService = emailProcessorService;
        this.mailVerificationWindow = mailVerificationWindow;
    }

    public void createEmail(EmailEvent event) {
        event.setVerified(false);
        emailProcessorService.sendVerificationEmail(event.getUserEmail(), event.getVerificationCode());
        emailProducer.produceEvent(event);
    }

    public void deleteEmail(String email) {
        Map<String, EmailEvent> emailsMap = getEmailsMap();

        EmailEvent event = emailsMap.get(email);
        if (event == null) {
            throw new EmailNotFoundException(email);
        }

        emailProducer.produceTombstoneEvent(event.getId().toString());
    }

    public void sendEmails(Map<String, BigDecimal> cryptoPrices) {
        getAllEmails()
                .filter(EmailEvent::isVerified)
                .forEach(email -> {
                    try {
                        emailProcessorService.sendPricesEmail(email.getUserEmail(), cryptoPrices);
                    } catch (Exception e) {
                        log.error("Email not sent", e);
                    }
                });
    }

    public Map<String, Long> getEmailStatuses() {
        Map<Boolean, Long> groupedEmails = getAllEmails()
                .collect(partitioningBy(
                        EmailEvent::isVerified,
                        Collectors.counting()
                ));

        Map<String, Long> response = new HashMap<>();
        response.put(VERIFIED_EMAIL_KEY, groupedEmails.getOrDefault(true, 0L));
        response.put(UNVERIFIED_EMAIL_KEY, groupedEmails.getOrDefault(false, 0L));
        return response;
    }

    public Stream<EmailEvent> getAllEmails() {
        return emailStore.getAll();
    }

    public ResponseEntity<String> verifyEmail(String email, String verificationCode) {
        Map<String, EmailEvent> emailsMap = getEmailsMap();
        EmailEvent event = emailsMap.get(email);

        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such email found to verify");
        }

        if (checkVerificationTimeHasExpired(event)) {
            return ResponseEntity.status(HttpStatus.GONE).body("Verification time has expired. Try verifying again.");
        }

        if (!verificationCode.equals(event.getVerificationCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Verification code incorrect. Try again");
        }

        event.setVerified(true);
        emailProducer.produceEvent(event);

        return ResponseEntity.ok("Email verified successfully");
    }

    private Map<String, EmailEvent> getEmailsMap() {
        return getAllEmails()
                .collect(Collectors.toMap(
                        EmailEvent::getUserEmail,
                        Function.identity()
                ));
    }

    private boolean checkVerificationTimeHasExpired(EmailEvent event) {
        Instant currentTime = Instant.now();
        Instant verificationCodeCreateTime = event.getVerificationCodeCreateTime();
        return Duration.between(currentTime, verificationCodeCreateTime)
                .compareTo(mailVerificationWindow) >= 0;
    }
}
