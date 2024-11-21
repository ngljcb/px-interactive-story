package com.sweng.InteractiveStory.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.config.http.SessionCreationPolicy;

// @Configuration
// public class SecurityConfig {

//     private final FirebaseTokenFilter firebaseTokenFilter;

//     public SecurityConfig(FirebaseTokenFilter firebaseTokenFilter) {
//         this.firebaseTokenFilter = firebaseTokenFilter;
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf().disable()
//             .authorizeHttpRequests()
//             // Permetti l'accesso pubblico alle pagine di login e registrazione e alle risorse statiche
//             .requestMatchers("/login.html", "/register.html", "/css/**", "/js/**").permitAll()
//             // Richiede autenticazione per tutte le altre richieste
//             .anyRequest().authenticated()
//             .and()
//             // Disabilita il form di login di default di Spring Security
//             .formLogin().disable()
//             // Configura la gestione delle sessioni come stateless
//             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//             .and()
//             // Aggiunge il filtro personalizzato per Firebase Token
//             .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll() // Consenti l'accesso pubblico
            .anyRequest().authenticated() // Richiedi autenticazione per altre richieste
            .and()
            .formLogin().disable() // Disabilita il form di login di default
            .httpBasic().disable() // Disabilita l'autenticazione di base
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Disabilita la gestione della sessione

        return http.build();
    }
}


