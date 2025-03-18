package crypto.newsletter.app.streams;

import crypto.newsletter.app.config.KafkaTopics;
import crypto.newsletter.app.model.CryptocurrencyEvent;
import crypto.newsletter.app.model.EmailEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamStore {

    public static final String CRYPTOCURRENCIES_STORE = "cryptocurrencies-store";
    public static final String EMAILS_STORE = "emails-store";

    private static final Serde<String> SERDE_STRING = Serdes.String();
    private static final Serde<CryptocurrencyEvent> CRYPTOCURRENCY_SERDE = new JsonSerde<>(CryptocurrencyEvent.class);
    private static final Serde<EmailEvent> EMAIL_SERDE = new JsonSerde<>(EmailEvent.class);

    private final KafkaTopics kafkaTopics;

    @Autowired
    public void processCryptocurrencyStore(StreamsBuilder streamsBuilder) {
        streamsBuilder.globalTable(
               kafkaTopics.getCryptocurrenciesTopic(),
               Consumed.with(SERDE_STRING, CRYPTOCURRENCY_SERDE),
               Materialized.as(CRYPTOCURRENCIES_STORE)
        );
    }

    @Autowired
    public void processEmailStore(StreamsBuilder streamsBuilder) {
        streamsBuilder.globalTable(
                kafkaTopics.getEmailsTopic(),
                Consumed.with(SERDE_STRING, EMAIL_SERDE),
                Materialized.as(EMAILS_STORE)
        );
    }
}
