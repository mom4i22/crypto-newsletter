package crypto.newsletter.app.exception;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String email) {
        super("No such email found: %s".formatted(email));
    }
}
