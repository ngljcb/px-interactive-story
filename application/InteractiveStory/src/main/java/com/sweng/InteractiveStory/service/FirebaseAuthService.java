package com.sweng.InteractiveStory.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.sweng.InteractiveStory.entity.user.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseAuthService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthService.class);

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    /**
     * Salva un utente nella collezione "Utente".
     *
     * @param uid          UID dell'utente.
     * @param email        Email dell'utente.
     * @param username     Nome dell'utente.
     * @param isStoryAdmin Indica se l'utente è un amministratore delle storie.
     */
    public void saveUser(String uid, String email, String username, boolean isStoryAdmin) {
        if (uid == null || uid.isEmpty()) {
            logger.error("UID cannot be null or empty.");
            throw new IllegalArgumentException("UID is required.");
        }

        logger.info("Saving user with UID: {}, email: {}, username: {}, isStoryAdmin: {}", uid, email, username, isStoryAdmin);

        Firestore dbFirestore = getFirestore();

        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", uid);
        userData.put("email", email);
        userData.put("username", username);
        userData.put("story-admin", isStoryAdmin);

        try {
            ApiFuture<WriteResult> future = dbFirestore.collection("Utente").document(uid).set(userData);
            WriteResult result = future.get();
            logger.info("User saved successfully at: {}", result.getUpdateTime());
        } catch (Exception e) {
            logger.error("Error saving user with UID: {}", uid, e);
            throw new RuntimeException("Error saving user to Firestore.", e);
        }
    }

    /**
     * Recupera un utente dalla collezione "Utente".
     *
     * @param uid UID dell'utente.
     * @return Oggetto Giocatore o Scrittore.
     */
    public Giocatore getUser(String uid) {
        if (uid == null || uid.isEmpty()) {
            logger.error("UID cannot be null or empty.");
            throw new IllegalArgumentException("UID is required.");
        }

        Firestore dbFirestore = getFirestore();
        DocumentReference documentReference = dbFirestore.collection("Utente").document(uid);

        try {
            DocumentSnapshot document = documentReference.get().get();
            if (document.exists()) {
                String email = document.contains("email") ? document.getString("email") : null;
                String username = document.contains("username") ? document.getString("username") : null;
                Boolean isStoryAdmin = document.contains("story-admin") ? document.getBoolean("story-admin") : false;

                if (email == null || username == null) {
                    logger.warn("Missing critical data for UID: {}. Email or username is null.", uid);
                    return null;
                }

                if (Boolean.TRUE.equals(isStoryAdmin)) {
                    logger.info("Scrittore found with UID: {}", uid);
                    return new Scrittore(uid, username, email, true);
                } else {
                    logger.info("Giocatore found with UID: {}", uid);
                    return new Giocatore(uid, username, email);
                }
            } else {
                logger.warn("No document found for UID: {}", uid);
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error retrieving user with UID: {}", uid, e);
            throw new RuntimeException("Error retrieving user from Firestore.", e);
        }
    }

    /**
     * Autentica un utente e restituisce i suoi dati.
     *
     * @param uid UID dell'utente.
     * @return Oggetto Giocatore o Scrittore autenticato.
     * @throws Exception Se l'utente non viene trovato.
     */
    public Giocatore authenticateUser(String uid) throws Exception {
        if (uid == null || uid.isEmpty()) {
            logger.error("UID cannot be null or empty.");
            throw new IllegalArgumentException("UID is required.");
        }

        Giocatore user = getUser(uid);
        if (user != null) {
            logger.info("User with UID: {} authenticated successfully.", uid);
            return user;
        } else {
            logger.warn("User with UID: {} not found.", uid);
            throw new Exception("Utente non trovato.");
        }
    }
}
