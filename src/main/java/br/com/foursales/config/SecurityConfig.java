package br.com.foursales.config;

import br.com.foursales.autentication.services.JwtFilter;
import br.com.foursales.dto.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Forma moderna, não obsoleta
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Liberar tudo em /auth
                        .requestMatchers(HttpMethod.POST, "/produto/**").hasRole(Role.ADMIN.getRole())
                        .requestMatchers(HttpMethod.PUT, "/produto/**").hasRole(Role.ADMIN.getRole())
                        .requestMatchers(HttpMethod.DELETE, "/produto/**").hasRole(Role.ADMIN.getRole())
                        .requestMatchers(HttpMethod.GET, "/produto/listarProdutos").hasAnyRole(Role.ADMIN.getRole(), Role.USER.getRole())
                        .requestMatchers(HttpMethod.PUT, "/pedido/criarPedido").hasAnyRole(Role.ADMIN.getRole(), Role.USER.getRole())



                        .anyRequest().authenticated()
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint((req, resp, e) -> {
//                            System.out.println("Acesso negado: " + req.getRequestURI() + " - " + e.getMessage());
//                            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
//                        })
//                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}