package com.sweng.InteractiveStory.controller;

import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweng.InteractiveStory.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        System.out.println("Received register request for email: " + email);

        try {
            // Prova a registrare l'utente
            String userId = authService.registerUser(email, password);
            System.out.println("User registered successfully with UID: " + userId);
            return ResponseEntity.ok("User registered successfully with UID: " + userId);
        } catch (FirebaseAuthException e) {
            System.err.println("FirebaseAuthException occurred: " + e.getMessage());
            System.err.println("Error Code: " + e.getAuthErrorCode());

            // Controlla se l'email esiste già
            if (e.getAuthErrorCode().name().equals("EMAIL_ALREADY_EXISTS")) {
                System.out.println("Email already registered: " + email);
                return ResponseEntity.status(400).body("Error: The email is already registered.");
            }

            // Gestione per altri errori
            return ResponseEntity.status(500).body("Error registering user: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected exception occurred: " + e.getMessage());
            return ResponseEntity.status(500).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestHeader("Authorization") String token) {
        System.out.println("Received Token: " + token); // Log per debug
        try {
            String userId = authService.verifyTokenAndGetUid(token);
            System.out.println("Login successful. User ID: " + userId); // Debug
            return ResponseEntity.ok("Login successful. User ID: " + userId);
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage()); // Log l'errore
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }
    }
}
