package crypto.newsletter.app.service;

import jakarta.mail.internet.MimeMessage;
import lombok.experimental.StandardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
public class EmailProcessorService {

    private static final String VERIFICATION_EMAIL_SUBJECT = "Verify email";
    private static final String PRICES_EMAIL_SUBJECT = "Minutely crypto prices update";
    private static final String REMINDER_TO_VERIFY_EMAIL_SUBJECT = "Reminder to verify email";

    private final String sender;
    private final JavaMailSender javaMailSender;

    public EmailProcessorService(JavaMailSender javaMailSender,
                                 @Value("${mail.sender}") String sender) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }

    public void sendVerificationEmail(String email, String verificationCode) {
        String content = formatContent(verificationCode);
        sendEmail(email, VERIFICATION_EMAIL_SUBJECT, content);
    }

    public void sendPricesEmail(String email, Map<String, BigDecimal> cryptoPrices) {
        String content = formatContent(cryptoPrices);
        sendEmail(email, PRICES_EMAIL_SUBJECT, content);
    }

    public void sendReminderEmail(String email) {
        String content = formatContent();
        sendEmail(email, REMINDER_TO_VERIFY_EMAIL_SUBJECT, content);
    }

    private void sendEmail(String email, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setFrom(sender);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            messageHelper.setTo(email);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new EmailNotSentException("Failed to send Email for prices to %s".formatted(email), e);
        }
    }

    private String formatContent(Map<String, BigDecimal> cryptoPrices) {
        StringBuilder html = new StringBuilder();
        html.append("<h3>Hello,</h3>");
        html.append("<p>Here are the latest crypto prices:</p>");
        html.append("<table border='1' style='border-collapse: collapse; width: 50%;'>");
        html.append("<tr><th style='padding: 8px; background-color: #f2f2f2;'>Crypto</th><th style='padding: 8px; background-color: #f2f2f2;'>Price (USD)</th></tr>");

        for (String crypto : cryptoPrices.keySet()) {
            BigDecimal price = cryptoPrices.get(crypto);
            html.append("<tr>");
            html.append("<td style='padding: 8px;'>").append(crypto.toUpperCase()).append("</td>");
            html.append("<td style='padding: 8px;'>$").append(price).append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("<p>Happy trading,</p>");
        html.append("<p><strong>Minutely Crypto Newsletter</strong></p>");

        return html.toString();
    }

    private String formatContent(String verificationCode) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2>Email Verification</h2>" +
                "<p>Thank you for signing up! Please verify your email using the following code:</p>" +
                "<h3 style='background-color: #f4f4f4; padding: 10px; border-radius: 5px;'>" +
                verificationCode +
                "</h3>" +
                "<p>This code will expire in <b>15 minutes</b>. If you didn’t request this, please ignore this email.</p>" +
                "<p>Regards,<br>Minutely Crypto Newsletter</p>" +
                "</body></html>";
    }

    private String formatContent() {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2>Reminder: Verify Your Email</h2>" +
                "<p>You haven't verified your email yet. Please complete the verification process to continue using our services.</p>" +
                "<p>Check your inbox for the original verification email.</p>" +
                "<p>This verification is required to activate your account.</p>" +
                "<p>Regards,<br>Your Company Team</p>" +
                "</body></html>";
    }

    @StandardException
    static class EmailNotSentException extends RuntimeException {
    }
}
