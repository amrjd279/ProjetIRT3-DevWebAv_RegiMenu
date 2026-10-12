package com.app.clientapi;

import com.app.clientapi.dto.SpoonacularResponseDto;
import com.app.clientapi.mapper.RecipeMapper;
import com.app.menu.model.MealDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Implémentation de RecipeGateway basée sur le RestClient de Spring Boot 3.2+.
 */
@Component
public class SpoonacularRestClient implements RecipeGateway {

    private static final Logger log = LoggerFactory.getLogger(SpoonacularRestClient.class);

    private final RestClient restClient;
    private final RecipeMapper recipeMapper;
    private final String apiKey;

    public SpoonacularRestClient(
            @Qualifier("spoonacularRestClientInstance") RestClient restClient,
            RecipeMapper recipeMapper,
            @Value("${spoonacular.api.key}") String apiKey) {
        this.restClient = restClient;
        this.recipeMapper = recipeMapper;
        this.apiKey = apiKey;
    }

    @Override
    public List<MealDto> getRandomRecipes(int count) {
        log.info("Appel externe Spoonacular pour récupérer {} recettes aléatoires...", count);

        SpoonacularResponseDto rawResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/random")
                        .queryParam("number", count)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    String errorBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.error("Erreur client Spoonacular [{}]: {}", response.getStatusCode(), errorBody);
                    throw new RuntimeException("Erreur API Spoonacular (" + response.getStatusCode() + "): " + errorBody);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    String errorBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.error("Erreur serveur Spoonacular [{}]: {}", response.getStatusCode(), errorBody);
                    throw new RuntimeException("API Spoonacular indisponible: " + errorBody);
                })
                .body(SpoonacularResponseDto.class);

        log.info("Recettes reçues avec succès depuis Spoonacular. Conversion via RecipeMapper...");
        return recipeMapper.toMealDtoList(rawResponse);
    }
}
