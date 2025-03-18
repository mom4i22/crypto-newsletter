package crypto.newsletter.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailEvent {

    private UUID id;
    private String userEmail;
    private boolean verified;
    private String verificationCode;
    private Instant verificationCodeCreateTime;
}
