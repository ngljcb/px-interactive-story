package com.sweng.InteractiveStory.controller;

import com.sweng.InteractiveStory.entity.user.Giocatore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@SessionAttributes("user") // Associa l'attributo "user" alla sessione
@RequestMapping("/session")
public class SessionController {

    @ModelAttribute("user") // Inizializza l'oggetto Giocatore
    public Giocatore setUpUser() {
        return new Giocatore(); // Crea un'istanza vuota di Giocatore
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
    @ResponseBody // Indica che la risposta è testo semplice o JSON
    public String saveUserToSession(@RequestBody Map<String, String> payload, @ModelAttribute("user") Giocatore user, Model model) {
        String userId = payload.get("userId");
        String email = payload.get("email");
        String username = payload.get("username");
        String idToken = payload.get("idToken");

        if (userId == null || userId.isEmpty() || idToken == null || idToken.isEmpty()) {
            return "Errore: UID o Token non forniti";
        }

        // Imposta i dati dell'utente
        user.setUid(userId);
        user.setEmail(email);
        user.setNome(username);

        // Salva il token come attributo separato se necessario
        model.addAttribute("idToken", idToken);

        return "Utente salvato correttamente nella sessione.";
    }

    /**
     * Recupera l'utente dalla sessione.
     *
     * @param user Oggetto Giocatore associato alla sessione
     * @return Informazioni sull'utente
     */
    @GetMapping("/user")
    @ResponseBody // Indica che la risposta è JSON
    public Giocatore getUserFromSession(@ModelAttribute("user") Giocatore user) {
        return user;
    }

    /**
     * Elimina l'utente dalla sessione (logout).
     *
     * @param model Modello per gestire gli attributi della sessione
     * @return Messaggio di conferma
     */
    @PostMapping("/logout")
    @ResponseBody // Indica che la risposta è testo semplice
    public String logout(Model model) {
        model.asMap().clear(); // Rimuove tutti gli attributi dalla sessione
        return "Sessione utente invalidata con successo.";
    }
}
