package com.app.admin.controller;

import com.app.admin.dto.AdminUserDto;
import com.app.admin.service.AdminService;
import com.app.profile.entity.UserRole;
import com.app.profile.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * API d'administration. L'identifiant de l'administrateur authentifié est
 * transmis par la couche d'authentification via X-Admin-User-Id.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AdminController {

    private final AdminService adminService;
    private final UserProfileRepository profileRepository;

    public AdminController(AdminService adminService, UserProfileRepository profileRepository) {
        this.adminService = adminService;
        this.profileRepository = profileRepository;
    }

    @GetMapping("/users")
    public List<AdminUserDto> listUsers(@RequestHeader(value = "X-Admin-User-Id", required = false) Long adminId) {
        requireAdmin(adminId);
        return adminService.listUsers();
    }

    @PutMapping("/users/{id}/suspend")
    public ResponseEntity<AdminUserDto> suspendUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-User-Id", required = false) Long adminId) {
        requireAdmin(adminId);
        return ResponseEntity.ok(adminService.suspendUser(id, adminId));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-User-Id", required = false) Long adminId) {
        requireAdmin(adminId);
        adminService.deleteUser(id, adminId);
        return ResponseEntity.noContent().build();
    }

    private void requireAdmin(Long adminId) {
        if (adminId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentification administrateur requise.");
        }
        boolean isAdmin = profileRepository.findById(adminId)
                .map(profile -> profile.getRole() == UserRole.ROLE_ADMIN)
                .orElse(false);
        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès réservé aux administrateurs.");
        }
    }
}
