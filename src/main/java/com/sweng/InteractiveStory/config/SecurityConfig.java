package com.sweng.InteractiveStory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabilita CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/css/**", "/js/**", "/login", "/register", "/").permitAll() // Permetti l'accesso a queste rotte
                .anyRequest().authenticated() // Richiedi autenticazione per il resto
            )
            .httpBasic(httpBasic -> httpBasic.disable()); // Disabilita autenticazione HTTP Basic
        return http.build();
    }
}
