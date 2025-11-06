package com.shantanu.journalApp.utils;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Secret key used to sign and verify JWT tokens
    private String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    // Generate signing key from secret string using HMAC-SHA256 algorithm
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Extract username (subject) from JWT token
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    // Extract expiration date from JWT token
    public Date extractExpiration(String token) {
        // if token expiration time is before than current time then token is expired return true.
        return extractAllClaims(token).getExpiration();
    }

    // Parse and validate JWT token to extract all claims (payload)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())    // set verification key for signature validation
                .build()                        // build immutable parser instance
                .parseSignedClaims(token)        // parse and verify signed JWT
                .getPayload();                   // extract actual claims (payload)
    }

    // Check if JWT token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate a new JWT token for given username
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // Create JWT with claims, subject, issue/expiry time, and signature
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)                                  // add payload data
                .subject(subject)                                // set subject (username)
                .header().empty().add("typ","JWT")               // explicitly set header type
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))  // issue time
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // expiry = 1 hour
                .signWith(getSigningKey())                       // sign using secret key
                .compact();                                      // build final JWT string
    }

    // Validate JWT token (only checks expiry here)
    public Boolean validateToken(String token) {
        // return true if token is not expired.
        return !isTokenExpired(token);
    }
}
