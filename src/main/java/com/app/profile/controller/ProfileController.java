package com.app.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.profile.dto.UpdatePreferencesDto;
import com.app.profile.dto.UserProfileDto;
import com.app.profile.service.ProfileService;
import com.app.security.SecurityUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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
    getCurrentProfile(
            @AuthenticationPrincipal SecurityUser currentUser) {
        return ResponseEntity.ok(
                profileService.getProfile(
                        currentUser.getId()
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
            @AuthenticationPrincipal SecurityUser currentUser,
            @RequestBody UpdatePreferencesDto dto
    ) {
        return ResponseEntity.ok(
                profileService.updatePreferences(
                        currentUser.getId(),
                        dto
                )
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
            @AuthenticationPrincipal SecurityUser currentUser,
            @RequestBody UpdatePreferencesDto dto
    ) {

        return updateCurrentProfile(currentUser, dto);
    }

}