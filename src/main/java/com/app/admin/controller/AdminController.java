package com.app.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.app.admin.dto.AdminUserDto;
import com.app.admin.service.AdminService;
import com.app.security.SecurityUser;

/**
 * API d'administration. L'identité et le rôle sont fournis par Spring Security.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<AdminUserDto> listUsers(
            @AuthenticationPrincipal SecurityUser currentUser) {
        return adminService.listUsers();
    }

    @PutMapping("/users/{id}/suspend")
    public ResponseEntity<AdminUserDto> suspendUser(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUser currentUser) {
        return ResponseEntity.ok(
                adminService.suspendUser(id, currentUser.getId()));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUser currentUser) {
        adminService.deleteUser(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
