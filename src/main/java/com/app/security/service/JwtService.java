package com.app.security.service;

import com.app.security.SecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilitaire JWT (JWT Utility) utilisant la bibliothèque jjwt 0.12.x.
 * Gère la création, l'extraction des informations (claims) et la validation cryptographique des jetons.
 */
@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Génère un jeton JWT signé contenant l'identifiant utilisateur et son rôle.
     */
    public String generateToken(SecurityUser user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole());

        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur (subject) depuis le jeton JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Vérifie que le jeton appartient bien à l'utilisateur et n'est pas expiré.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
