package crypto.newsletter.app.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Objects;
import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
public class KafkaStreamsConfig {

    @Bean
    public StreamsBuilderFactoryBeanConfigurer streamsBuilderFactoryBeanConfigurer() {
        return streamsBuilderFactory -> {
            Properties props = Objects.requireNonNull(streamsBuilderFactory.getStreamsConfiguration());
            props.put(DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
            props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
            props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        };
    }
}
