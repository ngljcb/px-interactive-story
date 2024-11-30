package com.sweng.InteractiveStory.controller;

import java.util.HashMap;
import java.util.Map;

import com.sweng.InteractiveStory.entity.game.*;
import com.sweng.InteractiveStory.entity.user.Giocatore;
import com.sweng.InteractiveStory.model.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;

@Controller
@SessionAttributes({"user", "partita"}) // Salva "user" e "partita" nella sessione
@RequestMapping("/api/game")
public class PartitaController {

    @ModelAttribute("partita")
    public Partita createPartita() {
        return new Partita(); // Crea un'istanza vuota di Partita
    }

    @ModelAttribute("user")
    public Giocatore createUser() {
        return new Giocatore(); // Crea un'istanza vuota di Giocatore
    }

    @PostMapping("/setup")
    public ResponseEntity<String> setup(@RequestBody Map<String, String> payload, 
                                        @ModelAttribute("user") Giocatore giocatore,
                                        @ModelAttribute("partita") Partita partita, Model model) {
        try {
            String idStoria = payload.get("idStoria");
            if (idStoria == null || idStoria.isEmpty()) {
                return ResponseEntity.badRequest().body("ID della storia non valido o nullo.");
            }

            partita.setGiocatore(giocatore);
            partita.setup(idStoria, new StoriaModel(), new ScenarioModel(), 
                        new SceltaIndovinelloModel(), new SceltaOggettoModel());

            model.addAttribute("partita", partita);

            return ResponseEntity.ok("Partita configurata correttamente con la storia: " + idStoria);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel setup della partita: " + e.getMessage());
        }
    }

    @PostMapping("/play")
    public ResponseEntity<String> play(@RequestBody Map<String, String> payload, 
                                       @ModelAttribute("partita") Partita partita) {
        try {
            // Verifica che la partita sia configurata
            if (partita.getScenarioCorrente() == null) {
                return ResponseEntity.badRequest().body("La partita non è stata configurata. Esegui prima il setup.");
            }

            String risposta = payload.get("risposta");
            System.out.println("Risposta ricevuta: " + risposta);

            String risultato = partita.play(risposta);
            System.out.println("Risultato del gameplay: " + risultato);

            return ResponseEntity.ok(risultato);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il gameplay: " + e.getMessage());
        }
    }

    @GetMapping("/current-scenario")
    public ResponseEntity<Object> getCurrentScenario(@ModelAttribute("partita") Partita partita) {
        try {
            // Verifica che la partita sia configurata
            if (partita.getScenarioCorrente() == null) {
                return ResponseEntity.badRequest().body("La partita non è stata configurata. Esegui prima il setup.");
            }

            Scenario scenarioCorrente = partita.getScenarioCorrente();

            Map<String, Object> scenarioData = new HashMap<>();
            scenarioData.put("id", scenarioCorrente.getId());
            scenarioData.put("descrizione", scenarioCorrente.getDescrizione());
            scenarioData.put("scelte", scenarioCorrente.getScelte() != null ? scenarioCorrente.getScelte().toString() : null);

            return ResponseEntity.ok(scenarioData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero dello scenario corrente: " + e.getMessage());
        }
    }

    @PostMapping("/end")
    public ResponseEntity<String> endGame(@ModelAttribute("partita") Partita partita, Model model) {
        try {
            // Verifica che la partita sia configurata
            if (partita.getScenarioCorrente() == null) {
                return ResponseEntity.badRequest().body("La partita non è stata configurata. Esegui prima il setup.");
            }

            partita.terminaPartita();
            model.asMap().remove("partita"); // Rimuove la partita dalla sessione

            return ResponseEntity.ok("Partita terminata con successo.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nella terminazione della partita: " + e.getMessage());
        }
    }
}
