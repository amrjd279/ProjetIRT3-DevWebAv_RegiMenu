package com.app.security.service;

import com.app.security.repository.AuthUserJdbcRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service standard Spring Security chargé de retrouver l'utilisateur en BDD
 * lors de la connexion ou de la vérification d'un jeton JWT.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserJdbcRepository authUserJdbcRepository;

    public CustomUserDetailsService(AuthUserJdbcRepository authUserJdbcRepository) {
        this.authUserJdbcRepository = authUserJdbcRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return authUserJdbcRepository.findByUsernameOrEmail(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + identifier));
    }
}
