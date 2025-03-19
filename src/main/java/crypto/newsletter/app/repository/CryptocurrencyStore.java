package crypto.newsletter.app.repository;

import crypto.newsletter.app.model.CryptocurrencyEvent;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Repository;

import static crypto.newsletter.app.streams.StreamStore.CRYPTOCURRENCIES_STORE;

@Repository
public class CryptocurrencyStore extends KafkaStore<String, CryptocurrencyEvent> {
    public CryptocurrencyStore(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        super(CRYPTOCURRENCIES_STORE, streamsBuilderFactoryBean);
    }
}
