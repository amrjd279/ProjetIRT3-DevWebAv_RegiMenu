package com.app.clientapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class SpoonacularClientConfig {

    @Value("${spoonacular.api.base-url:https://api.spoonacular.com}")
    private String baseUrl;

    @Bean
    public RestClient spoonacularRestClientInstance(RestClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}

