package crypto.newsletter.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptocurrencyEvent {

    private UUID id;
    private String name;
    private String coinGeckoId;
}
