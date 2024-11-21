package com.sweng.InteractiveStory.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String registerUser(String email, String password) throws FirebaseAuthException {
        System.out.println("Attempting to register user with email: " + email); // Debug
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        // Prova a creare un utente
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Firebase successfully created user with UID: " + userRecord.getUid());
        return userRecord.getUid();
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
