package com.app.profile.service;

import com.app.profile.dto.CreateProfileDto;
import com.app.profile.dto.UpdatePreferencesDto;
import com.app.profile.dto.UserProfileDto;
import com.app.profile.entity.DietaryPreferences;
import com.app.profile.entity.UserProfile;
import com.app.profile.repository.UserProfileRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service contenant la logique métier du profil.
 */
@Service
@Transactional
public class ProfileService {

    private final UserProfileRepository profileRepository;

    public ProfileService(UserProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Crée un nouveau profil.
     */
    public UserProfileDto createProfile(CreateProfileDto dto) {
        UserProfile profile = new UserProfile(
                dto.username(),
                dto.email()
        );

        DietaryPreferences preferences =
                new DietaryPreferences(
                        false, false, false, false, false,
                        dto.isVegetarian(),
                        dto.isVegan(),
                        dto.isGlutenFree(),
                        dto.isLactoseFree()
                );

        profile.setPreferences(preferences);

        UserProfile saved =
                profileRepository.save(profile);

        return toDto(saved);
    }

    /**
     * Récupère un profil grâce à son ID.
     */
    @Transactional(readOnly = true)
    public UserProfileDto getProfile(Long id) {
        UserProfile profile =
                profileRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Profil utilisateur introuvable pour l'id : " + id
                                )
                        );

        return toDto(profile);
    }

    /**
     * Méthode utilisée par MenuService.
     */
    @Transactional(readOnly = true)
    public DietaryPreferences getPreferences(Long id) {
        return profileRepository
                .findById(id)
                .map(UserProfile::getPreferences)
                .orElse(new DietaryPreferences());
    }

    /**
     * Modifie les préférences et les allergies.
     */
    public UserProfileDto updatePreferences(
            Long id,
            UpdatePreferencesDto dto
    ) {
        UserProfile profile =
                profileRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Profil utilisateur introuvable pour l'id : " + id
                                )
                        );

        DietaryPreferences preferences =
                profile.getPreferences();

        if (preferences == null) {
            preferences = new DietaryPreferences();
            profile.setPreferences(preferences);
        }

        // Mise à jour des allergies
        preferences.setAllergyPeanut(dto.isAllergyPeanut());
        preferences.setAllergyDairy(dto.isAllergyDairy());
        preferences.setAllergyGluten(dto.isAllergyGluten());
        preferences.setAllergyEgg(dto.isAllergyEgg());
        preferences.setAllergySeafood(dto.isAllergySeafood());

        // Mise à jour des préférences
        preferences.setVegetarian(dto.isVegetarian());
        preferences.setVegan(dto.isVegan());
        preferences.setGlutenFree(dto.isGlutenFree());
        preferences.setLactoseFree(dto.isLactoseFree());

        UserProfile saved =
                profileRepository.save(profile);

        return toDto(saved);
    }

    public void incrementRequestCount(Long id) {
        profileRepository
                .findById(id)
                .ifPresent(
                        UserProfile::incrementRequestCount
                );
    }

    public UserProfile getOrCreateDefaultProfile() {
        return profileRepository
                .findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    UserProfile defaultUser =
                            new UserProfile(
                                    "Visiteur",
                                    "user@regimenu.com"
                            );

                    return profileRepository.save(
                            defaultUser
                    );
                });
    }

    private UserProfileDto toDto(
            UserProfile profile
    ) {
        DietaryPreferences preferences =
                profile.getPreferences();

        if (preferences == null) {
            preferences = new DietaryPreferences();
        }

        return new UserProfileDto(
                profile.getId(),
                profile.getUsername(),
                profile.getEmail(),
                preferences.isAllergyPeanut(),
                preferences.isAllergyDairy(),
                preferences.isAllergyGluten(),
                preferences.isAllergyEgg(),
                preferences.isAllergySeafood(),
                preferences.isVegetarian(),
                preferences.isVegan(),
                preferences.isGlutenFree(),
                preferences.isLactoseFree(),
                profile.getRequestCount(),
                profile.getRole().name(),
                profile.getStatus().name()
        );
    }
}