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

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Endpoint pour récupérer le profil courant (ou profil par défaut).
     */
    @GetMapping
    public ResponseEntity<UserProfileDto> getCurrentProfile() {
        UserProfile defaultProfile = profileService.getOrCreateDefaultProfile();
        return ResponseEntity.ok(profileService.getProfile(defaultProfile.getId()));
    }

    /**
     * Endpoint pour récupérer un profil par son identifiant.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getProfileById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(profileService.getProfile(id));
    }

    /**
     * Endpoint pour créer un nouveau profil (v0.2).
     */
    @PostMapping
    public ResponseEntity<UserProfileDto> createProfile(@RequestBody CreateProfileDto dto) {
        return ResponseEntity.ok(profileService.createProfile(dto));
    }

    /**
     * Endpoint pour mettre à jour les préférences alimentaires du profil courant.
     */
    @PutMapping("/preferences")
    public ResponseEntity<UserProfileDto> updateCurrentPreferences(@RequestBody UpdatePreferencesDto dto) {
        UserProfile defaultProfile = profileService.getOrCreateDefaultProfile();
        return ResponseEntity.ok(profileService.updatePreferences(defaultProfile.getId(), dto));
    }

    /**
     * Endpoint pour mettre à jour les préférences alimentaires d'un utilisateur par son ID.
     */
    @PutMapping("/{id}/preferences")
    public ResponseEntity<UserProfileDto> updatePreferencesById(
            @PathVariable("id") Long id,
            @RequestBody UpdatePreferencesDto dto) {
        return ResponseEntity.ok(profileService.updatePreferences(id, dto));
    }
}

