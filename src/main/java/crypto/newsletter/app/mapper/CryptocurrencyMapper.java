package crypto.newsletter.app.mapper;

import crypto.newsletter.app.model.CryptocurrencyEvent;
import crypto.newsletter.app.rest.CreateCryptocurrencyRequest;
import crypto.newsletter.app.rest.CryptocurrencyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CryptocurrencyMapper {

    @Mapping(target = "id", expression = "java(generateId())")
    CryptocurrencyEvent toCryptocurrencyEvent(CreateCryptocurrencyRequest request);

    CryptocurrencyResponse toCryptocurrencyResponse(CryptocurrencyEvent event);

    default UUID generateId() {
        return UUID.randomUUID();
    }
}
