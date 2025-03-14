package br.com.foursales.autentication.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Service
public class JwtUtil {

    @Value("${secret.key}")
    private String secretKey;
    private  long UMA_HORA=1000 * 60 * 60;

    public String generateToken(String username, Collection<?> authorities) {
        String token = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(secretKey.getBytes(StandardCharsets.UTF_8)); // 32 bytes
            SecretKey key = Keys.hmacShaKeyFor(hash);
            token = Jwts.builder()
                    .setSubject(username)
                    .claim("role", authorities.iterator().next().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + UMA_HORA))
                    .signWith(key)
                    .compact();
        } catch (JwtException | NoSuchAlgorithmException exception) {
            throw new RuntimeException("Erro na criação de token");
        }

        return token;

    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }


}
