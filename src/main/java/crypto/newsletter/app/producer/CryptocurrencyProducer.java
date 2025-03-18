package crypto.newsletter.app.producer;

import crypto.newsletter.app.config.KafkaTopics;
import crypto.newsletter.app.model.CryptocurrencyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptocurrencyProducer {

    private final KafkaTopics kafkaTopics;
    private final KafkaTemplate<String, CryptocurrencyEvent> kafkaTemplate;

    public void produceEvent(CryptocurrencyEvent event) {
        produce(event.getId().toString(), event);
    }

    public void produceTombstoneEvent(String key) {
        produce(key, null);
    }

    private void produce(String key, CryptocurrencyEvent event) {
        kafkaTemplate.send(kafkaTopics.getCryptocurrenciesTopic(), key, event)
                .whenComplete((result, error) -> {
                   if (error == null) {
                       log.info("Produced cryptocurrency event with key {} and value {}", key, event);
                   } else {
                       log.error(
                               "Failed producing cryptocurrency event with key {} and value {}",
                               key, event, error
                       );
                   }
                });
    }
}
