package crypto.newsletter.app.service;

import crypto.newsletter.app.exception.CryptocurrencyNotFoundException;
import crypto.newsletter.app.model.CryptocurrencyEvent;
import crypto.newsletter.app.producer.CryptocurrencyProducer;
import crypto.newsletter.app.repository.CryptocurrencyStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptocurrencyService {

    private final CryptocurrencyStore cryptocurrencyStore;
    private final CryptocurrencyProducer cryptocurrencyProducer;

    public void createCryptocurrency(CryptocurrencyEvent event) {
        cryptocurrencyProducer.produceEvent(event);
    }

    public CryptocurrencyEvent getCryptocurrencyByName(String name) {
        Map<String, CryptocurrencyEvent> cryptocurrencyMap = getAllCryptocurrencies()
                .collect(Collectors.toMap(
                        CryptocurrencyEvent::getName,
                        Function.identity()
                ));

        CryptocurrencyEvent event = cryptocurrencyMap.get(name);
        if (event == null) {
            throw new CryptocurrencyNotFoundException(name);
        }

        return event;
    }

    public void deleteCryptocurrency(String name) {
        CryptocurrencyEvent eventToDelete = getCryptocurrencyByName(name);
        cryptocurrencyProducer.produceTombstoneEvent(eventToDelete.getId().toString());
    }

    public Stream<CryptocurrencyEvent> getAllCryptocurrencies() {
        return cryptocurrencyStore.getAll();
    }
}
