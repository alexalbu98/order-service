package com.polarbookshop.order_service.book;

import com.polarbookshop.order_service.config.ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Bean
    WebClient webClient(
            ClientProperties clientProperties,
            WebClient.Builder webClientBuilder
    ) {
        return webClientBuilder
                .baseUrl(clientProperties.catalogServiceURL().toString())
                .build();
    }
}
