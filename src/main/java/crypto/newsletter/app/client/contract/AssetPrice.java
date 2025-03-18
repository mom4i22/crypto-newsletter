package crypto.newsletter.app.client.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AssetPrice(@JsonProperty("usd") BigDecimal price) {
}
