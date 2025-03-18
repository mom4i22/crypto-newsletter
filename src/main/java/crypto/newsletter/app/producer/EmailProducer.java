package crypto.newsletter.app.producer;

import crypto.newsletter.app.config.KafkaTopics;
import crypto.newsletter.app.model.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailProducer {

    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;
    private final KafkaTopics kafkaTopics;

    public void produceEvent(EmailEvent event) {
        produce(event.getId().toString(), event);
    }

    public void produceTombstoneEvent(String key) {
        produce(key, null);
    }

    private void produce(String key, EmailEvent event) {
        kafkaTemplate.send(kafkaTopics.getEmailsTopic(), key, event)
                .whenComplete((result, error) -> {
                   if (error == null) {
                       log.info("Produced email event with key {} and value {}", key, event);
                   } else {
                       log.error(
                               "Failed producing email event with key {} and value {}",
                               key, event, error
                       );
                   }
                });
    }
}
