package crypto.newsletter.app.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaStoreService {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public <K, V> ReadOnlyKeyValueStore<K, V> getStore(String storeName) {
        KafkaStreams streams = Optional.ofNullable(streamsBuilderFactoryBean.getKafkaStreams()).orElseThrow();
        return streams.store(StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.keyValueStore()));
    }
}
