package com.app.security.config;

import com.app.security.repository.AuthUserJdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initialise un compte administrateur par défaut au démarrage s'il n'existe pas encore,
 * afin de faciliter le développement et les tests du module Admin.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final AuthUserJdbcRepository authUserJdbcRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(AuthUserJdbcRepository authUserJdbcRepository, PasswordEncoder passwordEncoder) {
        this.authUserJdbcRepository = authUserJdbcRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!authUserJdbcRepository.existsByUsername("admin")) {
            String hashedPassword = passwordEncoder.encode("admin123");
            authUserJdbcRepository.createUser("admin", "admin@regimenu.com", hashedPassword, "ROLE_ADMIN");
            log.info("Compte administrateur par défaut créé (username: admin, role: ROLE_ADMIN).");
        }
    }
}
