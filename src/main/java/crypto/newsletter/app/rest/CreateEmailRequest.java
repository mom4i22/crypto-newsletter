package crypto.newsletter.app.rest;

import crypto.newsletter.app.validator.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateEmailRequest {
    @NotBlank
    @ValidEmail
    private String userEmail;
}
