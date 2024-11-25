package com.sweng.InteractiveStory.controller;

import com.sweng.InteractiveStory.dto.UserRequest;
import com.sweng.InteractiveStory.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private FirebaseService firebaseService;

    /**
     * Endpoint per salvare i dati dell'utente su Firestore.
     *
     * @param userRequest Dati dell'utente da salvare
     * @return Messaggio di conferma o errore
     */
    @PostMapping("/firestore")
    public ResponseEntity<String> saveUserToFirestore(@RequestBody UserRequest userRequest) {
        try {
            // Salva i dati su Firestore tramite FirebaseService
            firebaseService.saveUser(
                userRequest.getUid(),
                userRequest.getEmail(),
                userRequest.getUsername(),
                userRequest.isStoryAdmin()
            );
            return ResponseEntity.ok("User data saved successfully to Firestore");
        } catch (Exception e) {
            // Restituisce un messaggio di errore in caso di fallimento
            return ResponseEntity.status(500).body("Error saving user data: " + e.getMessage());
        }
    }

    /**
     * Endpoint per verificare il funzionamento del controller e di FirebaseService (opzionale per debug).
     *
     * @param uid        UID dell'utente
     * @param email      Email dell'utente
     * @param username   Username dell'utente
     * @param isStoryAdmin Flag per indicare se l'utente è uno story-admin
     * @return Messaggio di successo o errore
     */
    @PostMapping("/test-save")
    public ResponseEntity<String> testSave(@RequestParam String uid, @RequestParam String email,
                                           @RequestParam String username, @RequestParam boolean isStoryAdmin) {
        try {
            // Chiamata di debug per testare il servizio
            firebaseService.saveUser(uid, email, username, isStoryAdmin);
            return ResponseEntity.ok("Test save: User data saved successfully to Firestore");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during test save: " + e.getMessage());
        }
    }
}
