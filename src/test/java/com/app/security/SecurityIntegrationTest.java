package com.app.security;

import com.app.menu.model.MealsResponseDto;
import com.app.menu.service.MenuService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests d'intégration de la chaîne Spring Security + JWT
 * (1/3 implémenté, 2/3 à compléter par l'étudiant).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcClient jdbcClient;

    @MockBean
    private MenuService menuService;

    @Test
    void shouldAllowPublicHomeMenuAndRejectUnauthenticatedRegenerate() throws Exception {
        when(menuService.getTodayMenu(null)).thenReturn(new MealsResponseDto(List.of()));

        // Route publique : doit répondre 200 OK sans jeton
        mockMvc.perform(get("/api/meals/today"))
                .andExpect(status().isOk());

        // Route protégée : doit répondre 401 Unauthorized sans jeton
        mockMvc.perform(post("/api/meals/regenerate"))
                .andExpect(status().isUnauthorized());
    }

    // =========================================================================
    // TODO: ÉTUDIANT (Test 2/3) — Tester le cycle complet Register -> Login -> GET /api/auth/me
    // Indice :
    // 1. Envoyer un POST /api/auth/register en JSON (username, email, password) -> attendre 201 Created.
    // 2. Envoyer un POST /api/auth/login en JSON (identifier = email ou username, password) -> attendre 200 OK
    //    et extraire le champ "token" de la réponse JSON (via ObjectMapper).
    // 3. Envoyer un GET /api/auth/me avec le header .header("Authorization", "Bearer " + token)
    //    et vérifier que le statut est 200 OK et que le username retourné correspond.
    // =========================================================================
    @Test
    void shouldRegisterLoginAndAccessAuthenticatedEndpoint() throws Exception {
        // Étape 1 : Register
        String registerJson = """
                {
                    "username": "testuser",
                    "email": "testuser@regimenu.com",
                    "password": "password"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(registerJson))
                .andExpect(status().isCreated());

        // Étape 2 : Login
        String loginJson = """
                {
                    "identifier": "testuser@regimenu.com",
                    "password": "password"
                }
                """;

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extraire le token
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(loginResponse);
        String token = jsonNode.get("token").asText();

        // Étape 3 : Accéder à l'endpoint authentifié
        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldForbidAdminEndpointForStandardUser() throws Exception {
        // Étape 1 : Register un utilisateur standard
        String registerJson = """
                {
                    "username": "standarduser",
                    "email": "standarduser@regimenu.com",
                    "password": "password"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(registerJson))
                .andExpect(status().isCreated());

        // Étape 2 : Login
        String loginJson = """
                {
                    "identifier": "standarduser@regimenu.com",
                    "password": "password"
                }
                """;

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extraire le token
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(loginResponse);
        String token = jsonNode.get("token").asText();

        // Étape 3 : Vérifier que le compte standard est refusé
        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowCorsForLocalhostAndRenderOriginsAndRejectUnauthorizedOrigin() throws Exception {
        when(menuService.getTodayMenu(null)).thenReturn(new MealsResponseDto(List.of()));

        // Origine localhost (Vite) autorisée
        mockMvc.perform(get("/api/meals/today").header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));

        // Origine Render (production et PR previews) autorisée
        mockMvc.perform(get("/api/meals/today").header("Origin", "https://projetirt3-frontend.onrender.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://projetirt3-frontend.onrender.com"));

        // Origine tierce non autorisée : rejetée immédiatement en 403 Forbidden par le CorsFilter
        mockMvc.perform(get("/api/meals/today").header("Origin", "https://unauthorized-domain.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldProtectProfileAndFavoritesEndpointsFromUnauthenticatedAccess() throws Exception {
        // GET /api/profile sans token -> 401
        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isUnauthorized());

        // GET /api/favorites sans token -> 401
        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isUnauthorized());

        // POST /api/favorites/{id} sans token -> 401
        mockMvc.perform(post("/api/favorites/101"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowAdminAccessForAdminRole() throws Exception {
        // Le compte 'admin' est seedé automatiquement au démarrage avec le mot de passe 'admin123'
        String loginJson = """
                {
                    "identifier": "admin",
                    "password": "admin123"
                }
                """;

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        String adminToken = objectMapper.readTree(loginResponse).get("token").asText();

        // Accès autorisé à la route d'administration réservée à ROLE_ADMIN
        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectAuthenticationForSuspendedUser() throws Exception {
        // 1. Inscription d'un utilisateur
        String registerJson = """
                {
                    "username": "blockeduser",
                    "email": "blocked@regimenu.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(registerJson))
                .andExpect(status().isCreated());

        // 2. Suspension du compte en base de données
        jdbcClient.sql("UPDATE user_profiles SET status = 'SUSPENDED' WHERE username = 'blockeduser'")
                .update();

        // 3. Tentative de connexion : rejetée avec 401 car isEnabled() renvoie false
        String loginJson = """
                {
                    "identifier": "blockeduser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }
}