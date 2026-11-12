package com.app.security;

import com.app.menu.model.MealsResponseDto;
import com.app.menu.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    /*
    @Test
    void shouldRegisterLoginAndAccessAuthenticatedEndpoint() throws Exception {
        // À compléter
    }
    */

    // =========================================================================
    // TODO: ÉTUDIANT (Test 3/3) — Vérifier l'isolation des rôles (ROLE_USER vs ROLE_ADMIN)
    // Indice :
    // 1. Créer / inscrire un utilisateur classique (ROLE_USER) et récupérer son JWT.
    // 2. Appeler une route réservée aux admins (ex: mockMvc.perform(get("/api/admin/users").header(...))).
    // 3. Vérifier que Spring Security bloque l'accès avec status().isForbidden() (403).
    // =========================================================================
    /*
    @Test
    void shouldForbidAdminEndpointForStandardUser() throws Exception {
        // À compléter
    }
    */
}
