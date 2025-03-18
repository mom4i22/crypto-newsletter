package crypto.newsletter.app.mapper;

import crypto.newsletter.app.model.EmailEvent;
import crypto.newsletter.app.rest.CreateEmailRequest;
import crypto.newsletter.app.rest.EmailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmailMapper {

    @Mapping(target = "id", expression = "java(generateId())")
    @Mapping(target = "verificationCode", expression = "java(generateVerificationCode())")
    @Mapping(target = "verificationCodeCreateTime", expression = "java(getCurrentTime())")
    EmailEvent toEmailEvent(CreateEmailRequest request);

    EmailResponse toEmailResponse(EmailEvent event);

    default Instant getCurrentTime() {
        return Instant.now();
    }

    default String generateVerificationCode() {
        return generateId().toString().substring(0, 6);
    }

    default UUID generateId() {
        return UUID.randomUUID();
    }
}
