package crypto.newsletter.app.controller.swagger;

import crypto.newsletter.app.rest.CreateCryptocurrencyRequest;
import crypto.newsletter.app.rest.CryptocurrencyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Cryptocurrency")
public interface CryptocurrencyControllerDoc {

    @Operation(summary = "Create cryptocurrency")
    @ApiResponse(responseCode = "201", description = "Successfully created cryptocurrency")
    @ApiResponse(responseCode = "500", description = "Error creating cryptocurrency")
    void createCryptocurrency(CreateCryptocurrencyRequest request);

    @Operation(summary = "Get cryptocurrency by name")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully fetched cryptocurrency",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CryptocurrencyResponse.class)
            )
    )
    @ApiResponse(responseCode = "500", description = "Failed fetching cryptocurrency by name")
    CryptocurrencyResponse getCryptocurrencyByName(String name);

    @Operation(summary = "Get all cryptocurrencies")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully fetched all cryptocurrencies",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = CryptocurrencyResponse.class))
            )
    )
    @ApiResponse(responseCode = "500", description = "Failed fetching all cryptocurrencies")
    List<CryptocurrencyResponse> getAllCryptocurrencies();

    @Operation(summary = "Delete cryptocurrency")
    @ApiResponse(responseCode = "204", description = "Successfully deleted cryptocurrency")
    @ApiResponse(responseCode = "500", description = "Failed deleting cryptocurrency")
    void deleteCryptocurrency(String name);
}
