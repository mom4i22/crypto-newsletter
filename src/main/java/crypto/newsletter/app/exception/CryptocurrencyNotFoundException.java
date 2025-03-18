package crypto.newsletter.app.exception;

public class CryptocurrencyNotFoundException extends RuntimeException {

    public CryptocurrencyNotFoundException(String name) {
        super("Cryptocurrency with name %s not found".formatted(name));
    }
}
