package br.com.foursales.autentication.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
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
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            token = Jwts.builder()
                    .setSubject(username)
                    .claim("role", authorities.iterator().next().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + UMA_HORA))
                    .signWith(key)
                    .compact();
        } catch (JwtException exception) {
            throw new RuntimeException("Erro na criação de token", exception);
        }

        return token;

    }

    public boolean isTokenExpired(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(secretKey.getBytes(StandardCharsets.UTF_8));
            Key key = Keys.hmacShaKeyFor(hash); // Consistência com geração
            System.out.println("Tamanho da chave em bytes (validação): " + hash.length);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro na validação do token: " + e.getMessage());
        }
    }

    public String extractUsername(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(secretKey.getBytes(StandardCharsets.UTF_8));
            Key key = Keys.hmacShaKeyFor(hash);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao extrair username do token: " + e.getMessage());
        }
    }

}
