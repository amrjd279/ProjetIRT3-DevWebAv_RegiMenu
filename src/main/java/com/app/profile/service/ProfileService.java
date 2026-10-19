package com.app.profile.service;

import com.app.profile.dto.CreateProfileDto;
import com.app.profile.dto.UpdatePreferencesDto;
import com.app.profile.dto.UserProfileDto;
import com.app.profile.entity.DietaryPreferences;
import com.app.profile.entity.UserProfile;
import com.app.profile.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {

    private final UserProfileRepository profileRepository;

    public ProfileService(UserProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public UserProfileDto createProfile(CreateProfileDto dto) {
        UserProfile profile = new UserProfile(dto.username(), dto.email());
        DietaryPreferences prefs = new DietaryPreferences(
                dto.isVegetarian(),
                dto.isVegan(),
                dto.isGlutenFree(),
                dto.isLactoseFree()
        );
        profile.setPreferences(prefs);
        UserProfile saved = profileRepository.save(profile);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public UserProfileDto getProfile(Long id) {
        UserProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profil utilisateur introuvable pour l'id : " + id));
        return toDto(profile);
    }

    @Transactional(readOnly = true)
    public DietaryPreferences getPreferences(Long id) {
        return profileRepository.findById(id)
                .map(UserProfile::getPreferences)
                .orElse(new DietaryPreferences());
    }

    public UserProfileDto updatePreferences(Long id, UpdatePreferencesDto dto) {
        UserProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profil utilisateur introuvable pour l'id : " + id));

        DietaryPreferences prefs = profile.getPreferences();
        prefs.setVegetarian(dto.isVegetarian());
        prefs.setVegan(dto.isVegan());
        prefs.setGlutenFree(dto.isGlutenFree());
        prefs.setLactoseFree(dto.isLactoseFree());

        return toDto(profile);
    }

    public void incrementRequestCount(Long id) {
        profileRepository.findById(id).ifPresent(UserProfile::incrementRequestCount);
    }

    /**
     * Récupère un profil utilisateur par défaut ou en crée un si aucun n'existe.
     * Pratique pour tester la v0.2 immédiatement.
     */
    public UserProfile getOrCreateDefaultProfile() {
        return profileRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    UserProfile defaultUser = new UserProfile("Visiteur", "user@regimenu.com");
                    return profileRepository.save(defaultUser);
                });
    }

    private UserProfileDto toDto(UserProfile profile) {
        DietaryPreferences prefs = profile.getPreferences() != null ? profile.getPreferences() : new DietaryPreferences();
        return new UserProfileDto(
                profile.getId(),
                profile.getUsername(),
                profile.getEmail(),
                prefs.isVegetarian(),
                prefs.isVegan(),
                prefs.isGlutenFree(),
                prefs.isLactoseFree(),
                profile.getRequestCount()
        );
    }
}

