package crypto.newsletter.app.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailResponse {
    private String userEmail;
    private boolean verified;
}

