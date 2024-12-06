package com.sweng.InteractiveStory.controller;

import com.sweng.InteractiveStory.entity.user.Giocatore;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@SessionAttributes("user")
@RequestMapping("/session")
public class SessionController {

    @ModelAttribute("user")
    public Giocatore setUpUser() {
        return new Giocatore();
    }

    /**
     * Salva l'utente nella sessione.
     *
     * @param payload Dati dell'utente
     * @param user    Oggetto Giocatore associato alla sessione
     * @param model   Modello per aggiornare i dati della sessione
     * @return Messaggio di successo o errore
     */
    @PostMapping("/saveUser")
    @ResponseBody
    public String saveUserToSession(@RequestBody Map<String, String> payload, @ModelAttribute("user") Giocatore user, Model model) {
        String userId = payload.get("userId");
        String email = payload.get("email");
        String username = payload.get("username");
        String idToken = payload.get("idToken");

        if (userId == null || userId.isEmpty() || idToken == null || idToken.isEmpty()) {
            return "Errore: UID o Token non forniti";
        }

        user.setUid(userId);
        user.setEmail(email);
        user.setNome(username);
        model.addAttribute("idToken", idToken);
        return "Utente salvato correttamente nella sessione.";
    }

    /**
     * Recupera l'utente dalla sessione.
     *
     * @param user Oggetto Giocatore associato alla sessione
     * @return Informazioni sull'utente o errore se non loggato
     */
    @GetMapping("/user")
    @ResponseBody // Indica che la risposta è JSON
    public ResponseEntity<Object> getUserFromSession(@ModelAttribute("user") Giocatore user) {
        if (user == null || user.getUid() == null || user.getUid().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error", "Utente non loggato."));
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Elimina l'utente dalla sessione (logout).
     *
     * @param model Modello per gestire gli attributi della sessione
     * @return Messaggio di conferma
     */
    @PostMapping("/logout")
    @ResponseBody 
    public String logout(Model model) {
        model.asMap().clear();
        return "Sessione utente invalidata con successo.";
    }
}
