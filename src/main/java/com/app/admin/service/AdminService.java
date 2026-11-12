package com.app.admin.service;

import com.app.admin.dto.AdminUserDto;
import com.app.profile.entity.AccountStatus;
import com.app.profile.entity.UserProfile;
import com.app.profile.entity.UserRole;
import com.app.profile.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Opérations de supervision des comptes.
 */
@Service
@Transactional
public class AdminService {

    private final UserProfileRepository profileRepository;

    public AdminService(UserProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminUserDto> listUsers() {
        return profileRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public AdminUserDto suspendUser(Long id, Long adminId) {
        UserProfile user = getUser(id);
        ensureNotSelf(id, adminId);
        profileRepository.updateStatus(id, AccountStatus.SUSPENDED);
        user.setStatus(AccountStatus.SUSPENDED);
        return toDto(user);
    }

    public void deleteUser(Long id, Long adminId) {
        UserProfile user = getUser(id);
        ensureNotSelf(id, adminId);
        if (user.getRole() == UserRole.ROLE_ADMIN) {
            throw new IllegalStateException("Un compte administrateur ne peut pas être supprimé.");
        }
        profileRepository.deleteById(id);
    }

    private UserProfile getUser(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable pour l'id : " + id));
    }

    private void ensureNotSelf(Long id, Long adminId) {
        if (id.equals(adminId)) {
            throw new IllegalStateException("Un administrateur ne peut pas modifier son propre compte.");
        }
    }

    private AdminUserDto toDto(UserProfile user) {
        return new AdminUserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus().name(),
                user.getRequestCount(),
                user.getCreatedAt()
        );
    }
}
