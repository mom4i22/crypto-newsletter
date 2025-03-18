package crypto.newsletter.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        Info info = new Info()
                .title("crypto-newsletter")
                .description("Swagger definition for CRUD operations with cryptocurrencies and emails");

        return new OpenAPI().info(info);
    }
}
