package com.sweng.InteractiveStory.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sweng.InteractiveStory.entity.game.*;
import com.sweng.InteractiveStory.entity.user.Giocatore;
import com.sweng.InteractiveStory.entity.utility.Oggetto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;

@Controller
@SessionAttributes({"user", "partita"}) // Salva "user" e "partita" nella sessione
@RequestMapping("/api/game")
public class PartitaController {

    private static final Logger logger = LoggerFactory.getLogger(PartitaController.class);

    @ModelAttribute("user")
    public Giocatore createUser() {
        logger.debug("Creazione di una nuova istanza di Giocatore.");
        return new Giocatore();
    }

    @PostMapping("/setup")
    public ResponseEntity<String> setup(@RequestBody Map<String, String> payload, 
                                        @ModelAttribute("user") Giocatore giocatore, Model model) {
        try {
            String idStoria = payload.get("idStoria");
            if (idStoria == null || idStoria.isEmpty()) {
                logger.debug("ID della storia non valido o nullo.");
                return ResponseEntity.badRequest().body("ID della storia non valido o nullo.");
            }

            Partita partita = new Partita();
            partita.setGiocatore(giocatore);
            partita.setup(idStoria);
            model.addAttribute("partita", partita);
            return ResponseEntity.ok("Partita configurata correttamente con la storia: " + idStoria);
        } catch (Exception e) {
            logger.error("Errore nel setup della partita.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel setup della partita: " + e.getMessage());
        }
    }

    @PostMapping("/play")
    public ResponseEntity<String> play(@RequestBody Map<String, String> payload, 
                                       @ModelAttribute("partita") Partita partita) {
        try {
            if (partita.getScenarioCorrente() == null) {
                logger.debug("Partita non configurata. Chiamata /play fallita.");
                return ResponseEntity.badRequest().body("La partita non è stata configurata. Esegui prima il setup.");
            }

            String risposta = payload.get("risposta");
            String risultato = partita.play(risposta);
            return ResponseEntity.ok(risultato);
        } catch (Exception e) {
            logger.error("Errore durante il gameplay.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il gameplay: " + e.getMessage());
        }
    }

    @GetMapping("/current-scenario")
    public ResponseEntity<Object> getCurrentScenario(@ModelAttribute("partita") Partita partita) {
        try {
            if (partita.getScenarioCorrente() == null) {
                logger.debug("Partita non configurata. Chiamata /current-scenario fallita.");
                return ResponseEntity.badRequest().body("La partita non è stata configurata. Esegui prima il setup.");
            }

            Scenario scenarioCorrente = partita.getScenarioCorrente();
            String oggettoTrovato = scenarioCorrente.getOggetto();

            // Aggiunge l'oggetto trovato all'inventario se presente
            if (oggettoTrovato != null && !oggettoTrovato.isEmpty()) {
                partita.aggiungiOggetto(new Oggetto(oggettoTrovato));
                logger.debug("Oggetto aggiunto all'inventario: {}", oggettoTrovato);
            }

            // Recupera l'inventario attuale, anche se vuoto
            String[] inventary = partita.getOggetti();
            logger.debug("Inventario attuale: {}", Arrays.toString(inventary));

            // Costruisce la risposta
            Map<String, Object> scenarioData = new HashMap<>();
            scenarioData.put("id", scenarioCorrente.getId());
            scenarioData.put("descrizione", scenarioCorrente.getDescrizione());
            scenarioData.put("tipoScelta", scenarioCorrente.getTipoScelta());
            scenarioData.put("ordernumber", scenarioCorrente.getOrderNumber());
            scenarioData.put("oggettotrovato", scenarioCorrente.getOggetto());
            scenarioData.put("inventario", inventary); // Sempre presente, anche se vuoto

            // Dati sulle scelte e sugli scenari successivi
            scenarioData.put("scelte", scenarioCorrente.getScelte() != null ? scenarioCorrente.getScelte().toString() : null);
            scenarioData.put("testodamostrare", scenarioCorrente.getScelte() != null ? scenarioCorrente.getScelte().testoDaMostrare() : null);
            scenarioData.put("scenarioCorretto", scenarioCorrente.getScelte() != null ? scenarioCorrente.getScelte().getProssimoScenarioCorretto() : null);
            scenarioData.put("scenarioErrato", scenarioCorrente.getScelte() != null ? scenarioCorrente.getScelte().getProssimoScenarioErrato() : null);

            return ResponseEntity.ok(scenarioData);
        } catch (Exception e) {
            logger.error("Errore nel recupero dello scenario corrente.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero dello scenario corrente: " + e.getMessage());
        }
    }


    @PostMapping("/save-progress")
    public ResponseEntity<String> saveProgress(@RequestBody Map<String, String> payload, 
                                               @ModelAttribute("user") Giocatore giocatore,
                                               @ModelAttribute("partita") Partita partita) {
        try {
            String userId = giocatore.getUid();
            if (userId == null || userId.isEmpty()) {
                logger.debug("Utente non autenticato.");
                return ResponseEntity.badRequest().body("Utente non autenticato. Effettua il login.");
            }

            String storyId = payload.get("storyId");
            String scenarioId = payload.get("scenarioId");

            if (storyId == null || storyId.isEmpty() || scenarioId == null || scenarioId.isEmpty()) {
                logger.debug("Dati incompleti per il salvataggio.");
                return ResponseEntity.badRequest()
                        .body("Dati incompleti. Assicurati che storyId e scenarioId siano presenti.");
            }

            partita.saveStory(userId, storyId, scenarioId);
            return ResponseEntity.ok("Progresso salvato con successo.");
        } catch (Exception e) {
            logger.error("Errore durante il salvataggio del progresso.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il salvataggio del progresso: " + e.getMessage());
        }
    }

    @PostMapping("/end")
    public ResponseEntity<String> endGame(@ModelAttribute("partita") Partita partita, Model model) {
        try {
            if (partita.getScenarioCorrente() == null) {
                logger.debug("Partita non configurata. Chiamata /end fallita.");
                return ResponseEntity.badRequest().body("La partita non è stata configurata. Esegui prima il setup.");
            }
            partita.terminaPartita();
            model.asMap().remove("partita");
            return ResponseEntity.ok("Partita terminata con successo.");
        } catch (Exception e) {
            logger.error("Errore nella terminazione della partita.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nella terminazione della partita: " + e.getMessage());
        }
    }
}
