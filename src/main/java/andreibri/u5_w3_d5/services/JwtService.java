package andreibri.u5_w3_d5.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        // Crea la chiave di firma a partire dal segreto nel file application.properties
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Genera il token JWT a partire dai dati dell'utente
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // Estrae lo username dal token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Controlla che il token sia valido e non scaduto
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Controlla se il token è scaduto
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Metodo MANCANTE nel tuo codice — legge tutti i "claims" (dati) dentro il token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)       // verifica la firma con la nostra chiave segreta
                .build()
                .parseSignedClaims(token)
                .getPayload();         // restituisce i dati contenuti nel token
    }
}