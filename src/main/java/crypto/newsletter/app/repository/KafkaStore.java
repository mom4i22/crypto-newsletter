package crypto.newsletter.app.repository;

import lombok.AllArgsConstructor;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
public abstract class KafkaStore<K, V> {

    private final String storeName;
    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public Stream<V> getAll() {
        ReadOnlyKeyValueStore<K, V> store = Objects.requireNonNull(streamsBuilderFactoryBean.getKafkaStreams())
                .store(StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.keyValueStore()));

        Stream.Builder<V> events = Stream.builder();
        try (KeyValueIterator<K, V> iterator = store.all()) {
            while (iterator.hasNext()) {
                V event = iterator.next().value;
                if (event != null) {
                    events.add(event);
                }
            }
        }

        return events.build();
    }
}
