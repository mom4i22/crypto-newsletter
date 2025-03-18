package crypto.newsletter.app.task;

import crypto.newsletter.app.model.EmailEvent;
import crypto.newsletter.app.service.EmailProcessorService;
import crypto.newsletter.app.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailUnverifiedUsersScheduler {

    private final EmailService emailService;
    private final EmailProcessorService emailProcessorService;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void emailUnverifiedUsers() {
        log.info("Starting scheduled emailing of unverified users");

        try {
            Set<String> emails = emailService.getAllEmails()
                    .filter(Predicate.not(EmailEvent::isVerified))
                    .map(EmailEvent::getUserEmail)
                    .collect(Collectors.toSet());

            emails.forEach(emailProcessorService::sendReminderEmail);
        } catch (Exception e) {
            log.error("Finished email unverified users");
        }

        log.info("Finished scheduled emailing of unverified users");
    }
}
