package crypto.newsletter.app.controller;

import crypto.newsletter.app.controller.swagger.CryptocurrencyControllerDoc;
import crypto.newsletter.app.mapper.CryptocurrencyMapper;
import crypto.newsletter.app.rest.CreateCryptocurrencyRequest;
import crypto.newsletter.app.rest.CryptocurrencyResponse;
import crypto.newsletter.app.service.CryptocurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/cryptocurrencies")
public class CryptocurrencyController implements CryptocurrencyControllerDoc {

    private final CryptocurrencyMapper cryptocurrencyMapper;
    private final CryptocurrencyService cryptocurrencyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCryptocurrency(@RequestBody CreateCryptocurrencyRequest request) {
        cryptocurrencyService.createCryptocurrency(cryptocurrencyMapper.toCryptocurrencyEvent(request));
    }

    @GetMapping
    public List<CryptocurrencyResponse> getAllCryptocurrencies() {
        return cryptocurrencyService.getCryptocurrencies()
                .map(cryptocurrencyMapper::toCryptocurrencyResponse)
                .toList();
    }

    @GetMapping("/{name}")
    public CryptocurrencyResponse getCryptocurrencyByName(@PathVariable String name) {
        return cryptocurrencyMapper.toCryptocurrencyResponse(
                cryptocurrencyService.getCryptocurrencyByName(name)
        );
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCryptocurrency(@PathVariable String name) {
        cryptocurrencyService.deleteCryptocurrency(name);
    }
}
