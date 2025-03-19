package crypto.newsletter.app.repository;

import crypto.newsletter.app.model.EmailEvent;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Repository;

import static crypto.newsletter.app.streams.StreamStore.EMAILS_STORE;

@Repository
public class EmailStore extends KafkaStore<String, EmailEvent> {
    public EmailStore(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        super(EMAILS_STORE, streamsBuilderFactoryBean);
    }
}
