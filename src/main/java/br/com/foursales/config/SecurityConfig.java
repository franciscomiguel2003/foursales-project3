package br.com.foursales.config;

import br.com.foursales.autentication.services.JwtFilter;
import br.com.foursales.dto.Role;
import jakarta.servlet.http.HttpServletResponse;
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
                        .requestMatchers("/pedido/buscarTotalFaturadoMes").permitAll()
                        .requestMatchers("/pedido/buscarTicketMedioUsuario").permitAll()
                        .requestMatchers("/pedido/buscarTop5UsuariosCompras").permitAll()
                        .requestMatchers(HttpMethod.POST, "/produto/**").hasRole(Role.ADMIN.getRole())
                        .requestMatchers(HttpMethod.PUT, "/produto/**").hasRole(Role.ADMIN.getRole())
                        .requestMatchers(HttpMethod.DELETE, "/produto/**").hasRole(Role.ADMIN.getRole())
                        .requestMatchers(HttpMethod.POST, "/pedido/criarPedido").hasRole(Role.USER.getRole())
                        .requestMatchers(HttpMethod.PUT, "/pedido/pagar").hasRole(Role.ADMIN.getRole())
                        .requestMatchers(HttpMethod.GET, "/produto/elastic/buscarProdutosElasticSearch/**").hasAnyRole(Role.ADMIN.getRole(), Role.USER.getRole())
                        .requestMatchers(HttpMethod.GET, "/produto/elastic/findAll/**").hasAnyRole(Role.ADMIN.getRole(), Role.USER.getRole())
                        .requestMatchers(HttpMethod.GET, "/produto/listarProdutos").hasAnyRole(Role.ADMIN.getRole(), Role.USER.getRole())
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Não Autorizado\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"Perfil não Autorizado\"}");
                        })
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
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