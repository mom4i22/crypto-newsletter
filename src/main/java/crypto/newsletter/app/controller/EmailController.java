package crypto.newsletter.app.controller;

import crypto.newsletter.app.controller.swagger.EmailControllerDoc;
import crypto.newsletter.app.mapper.EmailMapper;
import crypto.newsletter.app.rest.CreateEmailRequest;
import crypto.newsletter.app.rest.EmailResponse;
import crypto.newsletter.app.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/emails")
public class EmailController implements EmailControllerDoc {

    private final EmailMapper emailMapper;
    private final EmailService emailService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEmail(@Valid @RequestBody CreateEmailRequest request) {
        emailService.createEmail(emailMapper.toEmailEvent(request));
    }

    @GetMapping
    public List<EmailResponse> getAllEmails() {
        return emailService.getAllEmails()
                .map(emailMapper::toEmailResponse)
                .toList();
    }

    @PatchMapping("/{email}/{verificationCode}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email,
                                              @PathVariable String verificationCode) {
        return emailService.verifyEmail(email, verificationCode);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmail(@PathVariable String email) {
        emailService.deleteEmail(email);
    }


    @GetMapping("/status")
    public Map<String, Long> getEmailStatuses() {
        return emailService.getEmailStatuses();
    }
}
