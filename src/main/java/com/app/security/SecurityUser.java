package com.app.security;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Représentation de l'utilisateur authentifié exposée par le module Security.
 * Implémente l'interface standard UserDetails de Spring Security.
 */
public class SecurityUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String role;
    private final String status;

    public SecurityUser(Long id, String username, String email, String passwordHash, String role) {
        this(id, username, email, passwordHash, role, "ACTIVE");
    }

    public SecurityUser(
            Long id,
            String username,
            String email,
            String passwordHash,
            String role,
            String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = (role != null && !role.isBlank()) ? role : "ROLE_USER";
        this.status = (status != null && !status.isBlank()) ? status : "ACTIVE";
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String normalizedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return List.of(new SimpleGrantedAuthority(normalizedRole));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(status);
    }
}
