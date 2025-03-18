package crypto.newsletter.app.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCryptocurrencyRequest {

    private String name;
    private String coinGeckoId;
}
