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

@Service
public class FirebaseService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    // Scrive un utente nella collezione "Utente"
    public void saveUser(String uid, String email, String username, boolean isStoryAdmin) {
        logger.info("Entering saveUser with UID: {}, email: {}, username: {}, isStoryAdmin: {}", uid, email, username, isStoryAdmin);

        Firestore dbFirestore = FirestoreClient.getFirestore();
        System.out.println("############################################################################################ ");

        // Crea i dati da salvare
        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", uid);
        userData.put("email", email);
        userData.put("username", username);
        userData.put("story-admin", isStoryAdmin);

        System.out.println("Starting Firestore write request for UID: " + uid);
        System.out.println("Data to save: " + userData);

        logger.info("Prepared userData: {}", userData);

        // Scrive i dati nella collezione "Utente" con UID come ID del documento
        ApiFuture<WriteResult> future = dbFirestore.collection("Utente").document(uid).set(userData);

        try {
            WriteResult result = future.get();
            System.out.println("Document written successfully at: " + result.getUpdateTime());
        } catch (Exception e) {
            System.err.println("Error writing to Firestore for UID: " + uid);
            e.printStackTrace();
        }
    }

    // Recupera un utente dalla collezione "Utente"
    public Giocatore getUser(String uid) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("Utente").document(uid);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                String email = document.getString("email");
                String username = document.getString("username");
                boolean isStoryAdmin = document.getBoolean("story-admin");

                if (isStoryAdmin) {
                    return new Scrittore(username, email, true);
                } else {
                    return new Giocatore(username, email);
                }
            } else {
                System.out.println("No such document found for UID: " + uid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error retrieving user from Firestore: " + e.getMessage());
        }
        return null;
    }
}

