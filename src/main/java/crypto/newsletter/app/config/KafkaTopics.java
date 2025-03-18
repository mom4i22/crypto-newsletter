package crypto.newsletter.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KafkaTopics {

    @Value("${kafka.topic.cryptocurrencies-topic}")
    private String cryptocurrenciesTopic;

    @Value("${kafka.topic.emails-topic}")
    private String emailsTopic;
}
