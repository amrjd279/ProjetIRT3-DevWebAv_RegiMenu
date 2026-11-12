package com.app.security.controller;

import com.app.security.SecurityUser;
import com.app.security.dto.AuthResponse;
import com.app.security.dto.LoginRequest;
import com.app.security.dto.RegisterRequest;
import com.app.security.dto.UserInfoResponse;
import com.app.security.repository.AuthUserJdbcRepository;
import com.app.security.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Contrôleur gérant l'inscription, la connexion JWT et la récupération du profil authentifié.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUserJdbcRepository authUserJdbcRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            AuthUserJdbcRepository authUserJdbcRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.authUserJdbcRepository = authUserJdbcRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (request == null || isBlank(request.username()) || isBlank(request.email()) || isBlank(request.password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username, email et password sont obligatoires");
        }
        String username = request.username().trim();
        String email = request.email().trim();

        if (authUserJdbcRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce nom d'utilisateur est déjà utilisé");
        }
        if (authUserJdbcRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé");
        }

        String passwordHash = passwordEncoder.encode(request.password());
        SecurityUser created = authUserJdbcRepository.createUser(username, email, passwordHash, "ROLE_USER");
        String token = jwtService.generateToken(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(toAuthResponse(token, created));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        if (request == null || isBlank(request.identifier()) || isBlank(request.password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identifiant et mot de passe obligatoires");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.identifier().trim(), request.password()));

            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(toAuthResponse(token, user));
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides", ex);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal SecurityUser currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new UserInfoResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getRole()));
    }

    private AuthResponse toAuthResponse(String token, SecurityUser user) {
        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
