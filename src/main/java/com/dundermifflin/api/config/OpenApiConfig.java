package com.dundermifflin.api.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dunder Mifflin API")
                        .description("Production-ready API for Dunder Mifflin. Use Swagger UI to explore endpoints.")
                        .version("v1")
                        .contact(new Contact().name("Dunder Mifflin IT").email("it@dundermifflin.com"))
                );
    }
}
