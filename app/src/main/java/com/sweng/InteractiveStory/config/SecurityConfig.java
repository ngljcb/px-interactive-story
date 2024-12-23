package com.sweng.InteractiveStory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabilita CSRF per test o per API
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configura CORS
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/session/**",
                    "/auth/**",
                    "/assets/**",
                    "/css/**",
                    "/js/**",
                    "/login",
                    "/register",
                    "/game",
                    "/create",
                    "/modify",
                    "/settings",
                    "/play",
                    "/",
                    "/index",
                    "/api/game/**",
                    "/api/game",
                    "/api/settings",
                    "/api/stories",
                    "/api/stories/{id}/modify",
                    "/api/stories/**"
                ).permitAll() // Permetti l'accesso a queste rotte
                .anyRequest().authenticated() // Richiedi autenticazione per il resto
            )
            .httpBasic(httpBasic -> httpBasic.disable()); // Disabilita HTTP Basic

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080")); // Origini permessi
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Metodi permessi
        configuration.setAllowedHeaders(List.of("*")); // Permetti tutti gli header
        configuration.setAllowCredentials(true); // Permetti credenziali per sessioni

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applica configurazione a tutte le rotte
        return source;
    }
}
