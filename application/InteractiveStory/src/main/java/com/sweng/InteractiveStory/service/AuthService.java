package com.sweng.InteractiveStory.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweng.InteractiveStory.entity.user.Giocatore;

@Service
public class AuthService {

    @Autowired
    private FirebaseAuthService firebaseService;

    // Registra un nuovo utente
    public String registerUser(String email, String password) throws FirebaseAuthException {
        System.out.println("Attempting to register user with email: " + email); // Debug
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        // Creare un utente
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        String uid = userRecord.getUid();
        String username = email.split("@")[0]; // Default username
        firebaseService.saveUser(uid, email, username, false); // Non è story-admin per default

        System.out.println("User registered successfully and saved in Firestore.");
        return uid;
    }

    public Giocatore getUserFromToken(String token) throws FirebaseAuthException {
        // Verifica il token e recupera l'UID
        String uid = verifyTokenAndGetUid(token);

        // Recupera l'utente da Firestore
        return firebaseService.getUser(uid);
    }

    public String verifyTokenAndGetUid(String token) throws FirebaseAuthException {
        if (token.startsWith("Bearer ")) {
            String idToken = token.substring(7); // Rimuove il prefisso "Bearer "
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            return decodedToken.getUid(); // Restituisce l'UID dell'utente autenticato
        } else {
            throw new IllegalArgumentException("Token must start with 'Bearer '");
        }
    }
}
