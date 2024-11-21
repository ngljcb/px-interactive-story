package com.sweng.InteractiveStory.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String registerUser(String email, String password) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        return userRecord.getUid();
    }

    public String loginUser(String email, String password) {
        // Nota: Firebase Admin SDK non supporta direttamente il login con email e password.
        // Dovrai usare il Client SDK di Firebase o l'autenticazione del token idToken.
        return "Per il login utilizza il Firebase Client SDK per ottenere l'idToken";
    }
}
