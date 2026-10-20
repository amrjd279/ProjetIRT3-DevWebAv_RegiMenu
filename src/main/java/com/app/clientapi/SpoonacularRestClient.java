package com.app.clientapi;

import com.app.clientapi.dto.SpoonacularRecipeDetailDto;
import com.app.clientapi.dto.SpoonacularResponseDto;
import com.app.clientapi.mapper.RecipeMapper;
import com.app.menu.RecipeGateway;
import com.app.menu.model.MealDetailDto;
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
    public List<MealDto> getRandomRecipes(int count, List<String> tags) {
        String tagsParam = (tags != null && !tags.isEmpty()) ? String.join(",", tags) : null;
        log.info("Appel externe Spoonacular pour récupérer {} recettes (tags: {})...", count, tagsParam);

        SpoonacularResponseDto rawResponse = restClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/recipes/random")
                            .queryParam("number", count)
                            .queryParam("apiKey", apiKey);
                    if (tagsParam != null && !tagsParam.isBlank()) {
                        builder.queryParam("tags", tagsParam);
                    }
                    return builder.build();
                })
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

    @Override
    public MealDetailDto getRecipeDetails(Long id) {
        log.info("Appel externe Spoonacular pour récupérer les détails de la recette id={}...", id);

        SpoonacularRecipeDetailDto rawResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/{id}/information")
                        .queryParam("apiKey", apiKey)
                        .build(id))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    String errorBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.error("Erreur client Spoonacular [{}]: {}", response.getStatusCode(), errorBody);
                    throw new RuntimeException("Recette introuvable ou erreur Spoonacular (" + response.getStatusCode() + "): " + errorBody);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    String errorBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.error("Erreur serveur Spoonacular [{}]: {}", response.getStatusCode(), errorBody);
                    throw new RuntimeException("API Spoonacular indisponible: " + errorBody);
                })
                .body(SpoonacularRecipeDetailDto.class);

        log.info("Détails reçus pour la recette id={}. Conversion via RecipeMapper...", id);
        return recipeMapper.toMealDetailDto(rawResponse);
    }
}
