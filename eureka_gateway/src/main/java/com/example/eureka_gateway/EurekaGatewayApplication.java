package com.example.eureka_gateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@OpenAPIDefinition
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/send/api-docs").and().method(HttpMethod.GET).uri("lb://MSG-KAFKA-PRODUCER"))
                .route(r -> r.path("/send/**").and().method(HttpMethod.POST).uri("lb://MSG-KAFKA-PRODUCER"))
                .route(r -> r.path("/auth/**").uri("lb://AUTH-SERVER"))
                .build();
    }
}
