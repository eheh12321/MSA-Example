package com.example.kafka_producer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

    @Value("${openapi.service.title}")
    private String title;

    @Value("${openapi.service.serviceVersion}")
    private String serviceVersion;

    @Value("${openapi.service.url}")
    private String url;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .servers(List.of(new Server().url(url)))
                .info(new Info().title(title).version(serviceVersion));
    }
}
