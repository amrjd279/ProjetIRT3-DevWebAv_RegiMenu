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
                                        "Profil utilisateur introuvable pour l'id : "
                                                + id
                                )
                        );

        return toDto(profile);
    }

    /**
     * Méthode utilisée par MenuService.
     *
     * Elle permet de récupérer les préférences
     * d'un utilisateur.
     */
    @Transactional(readOnly = true)
    public DietaryPreferences getPreferences(Long id) {

        return profileRepository
                .findById(id)
                .map(UserProfile::getPreferences)
                .orElse(new DietaryPreferences());
    }

    /**
     * US4.2
     *
     * Modifie les préférences alimentaires.
     */
    public UserProfileDto updatePreferences(
            Long id,
            UpdatePreferencesDto dto
    ) {

        /*
         * Étape 1 :
         * récupérer le profil.
         */
        UserProfile profile =
                profileRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Profil utilisateur introuvable pour l'id : "
                                                + id
                                )
                        );

        /*
         * Étape 2 :
         * récupérer les préférences.
         */
        DietaryPreferences preferences =
                profile.getPreferences();

        /*
         * Étape 3 :
         * modifier les quatre préférences.
         */
        preferences.setVegetarian(
                dto.isVegetarian()
        );

        preferences.setVegan(
                dto.isVegan()
        );

        preferences.setGlutenFree(
                dto.isGlutenFree()
        );

        preferences.setLactoseFree(
                dto.isLactoseFree()
        );

        /*
         * Étape 4 :
         * sauvegarder le profil.
         *
         * Grâce à CascadeType.ALL,
         * les préférences sont également sauvegardées.
         */
        UserProfile saved =
                profileRepository.save(profile);

        /*
         * Étape 5 :
         * transformer l'entité en DTO.
         */
        return toDto(saved);
    }

    /**
     * Incrémente le compteur de requêtes.
     *
     * Cette méthode est déjà utilisée par le projet.
     */
    public void incrementRequestCount(Long id) {

        profileRepository
                .findById(id)
                .ifPresent(
                        UserProfile::incrementRequestCount
                );
    }

    /**
     * Profil temporaire utilisé tant que
     * l'authentification réelle n'est pas branchée.
     *
     * IMPORTANT :
     * Cette méthode est temporaire.
     */
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

    /**
     * Transforme une entité UserProfile
     * en UserProfileDto.
     */
    private UserProfileDto toDto(
            UserProfile profile
    ) {

        DietaryPreferences preferences =
                profile.getPreferences();

        return new UserProfileDto(

                profile.getId(),

                profile.getUsername(),

                profile.getEmail(),

                preferences.isVegetarian(),

                preferences.isVegan(),

                preferences.isGlutenFree(),

                preferences.isLactoseFree(),

                profile.getRequestCount()
        );
    }
}