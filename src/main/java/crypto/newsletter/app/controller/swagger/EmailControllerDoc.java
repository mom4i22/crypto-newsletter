package crypto.newsletter.app.controller.swagger;

import crypto.newsletter.app.rest.CreateEmailRequest;
import crypto.newsletter.app.rest.EmailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Email")
public interface EmailControllerDoc {

    @Operation(summary = "Create email")
    @ApiResponse(responseCode = "201", description = "Successfully created email")
    @ApiResponse(responseCode = "400", description = "Invalid email format")
    @ApiResponse(responseCode = "500", description = "Error creating email")
    void createEmail(CreateEmailRequest request);

    @Operation(summary = "Get all emails")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully fetched all emails",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = EmailResponse.class))
            )
    )
    @ApiResponse(responseCode = "500", description = "Failed fetching all emails")
    List<EmailResponse> getAllEmails();

    @Operation(summary = "Verify email")
    @ApiResponse(responseCode = "200", description = "Successfully verified email")
    @ApiResponse(responseCode = "404", description = "No such email found to verify")
    @ApiResponse(responseCode = "410", description = "Email verification window has expired")
    @ApiResponse(responseCode = "401", description = "Incorrect verification code")
    @ApiResponse(responseCode = "500", description = "Failed verifying email")
    ResponseEntity<String> verifyEmail(String email, String verificationCode);

    @Operation(summary = "See how many verified and unverified emails there are in the system")
    @ApiResponse(responseCode = "200", description = "Successfully fetched email statuses")
    @ApiResponse(responseCode = "500", description = "Failed to fetch email statuses")
    Map<String, Long> getEmailStatuses();

    @Operation(summary = "Delete email")
    @ApiResponse(responseCode = "204", description = "Successfully deleted email")
    @ApiResponse(responseCode = "500", description = "Failed deleting email")
    void deleteEmail(String email);
}
