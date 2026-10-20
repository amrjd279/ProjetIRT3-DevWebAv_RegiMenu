package com.app.profile.controller;

import com.app.profile.dto.CreateProfileDto;
import com.app.profile.dto.UpdatePreferencesDto;
import com.app.profile.dto.UserProfileDto;
import com.app.profile.entity.UserProfile;
import com.app.profile.service.ProfileService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST du module Profile.
 *
 * US4.2 :
 *
 * GET /api/profile
 * PUT /api/profile
 */
@RestController
@RequestMapping("/api/profile")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://localhost:3000"
        }
)
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(
            ProfileService profileService
    ) {
        this.profileService = profileService;
    }

    /**
     * US4.2
     *
     * GET /api/profile
     *
     * Récupère le profil courant.
     */
    @GetMapping
    public ResponseEntity<UserProfileDto>
    getCurrentProfile() {

        /*
         * Pour l'instant, l'authentification
         * n'est pas encore branchée.
         *
         * On utilise donc un profil temporaire.
         */
        UserProfile profile =
                profileService.getOrCreateDefaultProfile();

        return ResponseEntity.ok(
                profileService.getProfile(
                        profile.getId()
                )
        );
    }

    /**
     * US4.2
     *
     * PUT /api/profile
     *
     * Modifie les préférences alimentaires.
     */
    @PutMapping
    public ResponseEntity<UserProfileDto>
    updateCurrentProfile(
            @RequestBody UpdatePreferencesDto dto
    ) {

        UserProfile profile =
                profileService.getOrCreateDefaultProfile();

        return ResponseEntity.ok(
                profileService.updatePreferences(
                        profile.getId(),
                        dto
                )
        );
    }

    /**
     * Endpoint existant.
     *
     * GET /api/profile/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto>
    getProfileById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                profileService.getProfile(id)
        );
    }

    /**
     * Endpoint existant.
     *
     * POST /api/profile
     */
    @PostMapping
    public ResponseEntity<UserProfileDto>
    createProfile(
            @RequestBody CreateProfileDto dto
    ) {

        return ResponseEntity.ok(
                profileService.createProfile(dto)
        );
    }

    /**
     * Ancien endpoint conservé
     * pour éviter de casser le frontend existant.
     *
     * PUT /api/profile/preferences
     */
    @PutMapping("/preferences")
    public ResponseEntity<UserProfileDto>
    updateCurrentPreferences(
            @RequestBody UpdatePreferencesDto dto
    ) {

        return updateCurrentProfile(dto);
    }

    /**
     * Ancien endpoint conservé.
     *
     * PUT /api/profile/{id}/preferences
     */
    @PutMapping("/{id}/preferences")
    public ResponseEntity<UserProfileDto>
    updatePreferencesById(
            @PathVariable Long id,
            @RequestBody UpdatePreferencesDto dto
    ) {

        return ResponseEntity.ok(
                profileService.updatePreferences(
                        id,
                        dto
                )
        );
    }
}