package br.com.foursales.autentication.services;


import br.com.foursales.model.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class JwtUtil {

    @Value("${secret.key}")
    private String secretKey;
    private  long UMA_HORA=1000 * 60 * 60;

    public String generateToken(String username, Collection<?> authorities) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", authorities.iterator().next().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + UMA_HORA))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
