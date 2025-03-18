package crypto.newsletter.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableFeignClients
@EnableScheduling
@EnableKafkaStreams
@SpringBootApplication(scanBasePackages = "crypto.newsletter.app")
public class CryptoNewsletterApplication {
	public static void main(String[] args) {
		SpringApplication.run(CryptoNewsletterApplication.class, args);
	}
}
