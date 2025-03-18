package crypto.newsletter.app.service;

import crypto.newsletter.app.exception.CryptocurrencyNotFoundException;
import crypto.newsletter.app.model.CryptocurrencyEvent;
import crypto.newsletter.app.producer.CryptocurrencyProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static crypto.newsletter.app.streams.StreamStore.CRYPTOCURRENCIES_STORE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptocurrencyService {

    private final KafkaStoreService kafkaStoreService;
    private final CryptocurrencyProducer cryptocurrencyProducer;

    public void createCryptocurrency(CryptocurrencyEvent event) {
        cryptocurrencyProducer.produceEvent(event);
    }

    public CryptocurrencyEvent getCryptocurrencyByName(String name) {
        Map<String, CryptocurrencyEvent> cryptocurrencyMap = getCryptocurrencies()
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

    public Stream<CryptocurrencyEvent> getCryptocurrencies() {
        ReadOnlyKeyValueStore<String, CryptocurrencyEvent> store = kafkaStoreService.getStore(CRYPTOCURRENCIES_STORE);

        Stream.Builder<CryptocurrencyEvent> cryptocurrencies = Stream.builder();
        try (KeyValueIterator<String, CryptocurrencyEvent> iterator = store.all()) {
            while (iterator.hasNext()) {
                CryptocurrencyEvent event = iterator.next().value;
                if (event != null) {
                    cryptocurrencies.add(event);
                }
            }
        }

        return cryptocurrencies.build();
    }
}
