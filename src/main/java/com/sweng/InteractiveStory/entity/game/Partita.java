package com.sweng.InteractiveStory.entity.game;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sweng.InteractiveStory.entity.option.Scelta;
import com.sweng.InteractiveStory.entity.user.*;
import com.sweng.InteractiveStory.entity.utility.*;
import com.sweng.InteractiveStory.model.*;

public class Partita {
    private boolean stato;
    private Giocatore giocatore;
    private Storia storia;
    private Scenario scenarioCorrente;
    private Inventario inventario;
    private StoriaModel storiaModel;

    private static final Logger logger = LoggerFactory.getLogger(Partita.class);

    public Partita(Giocatore giocatore) {
        this.stato = true;
        this.giocatore = giocatore;
        this.inventario = new Inventario();
        storiaModel = new StoriaModel();
    }

    public Partita() {
        this.stato = true;
        this.inventario = new Inventario();
        storiaModel = new StoriaModel();
    }

    /**
     * Imposta la storia e i suoi scenari.
     */
    public void setup(String idStoria) throws Exception {
        if (idStoria == null || idStoria.isEmpty()) {
            logger.error("ID della storia non valido o nullo.");
            throw new IllegalArgumentException("ID della storia non valido o nullo.");
        }

        logger.debug("Inizio setup per la storia con ID: {}", idStoria);

        try {
            this.storia = new Storia();
            storia.getStoria(idStoria);
            logger.debug("Storia caricata con successo: {}", storia.getTitolo());
        } catch (Exception e) {
            logger.error("Errore durante il caricamento della storia.", e);
            throw new Exception("Errore durante il caricamento della storia: " + e.getMessage());
        }

        try {
            storia.setScenari(idStoria);
            logger.debug("Scenari caricati con successo: {} scenari trovati.", storia.getScenariList().size());
        } catch (Exception e) {
            logger.error("Errore durante il caricamento degli scenari.", e);
            throw new Exception("Errore durante il caricamento degli scenari: " + e.getMessage());
        }

        for (Scenario scenario : storia.getScenariList()) {
            String tipoScelta = scenario.getTipoScelta();

            if ("indovinello".equalsIgnoreCase(tipoScelta)) {
                try {
                    scenario.getIndovinello(scenario.getId());
                    logger.debug("Indovinello configurato per lo scenario: {}", scenario.getId());
                } catch (Exception e) {
                    logger.error("Errore durante la configurazione dell'indovinello.", e);
                    throw new Exception("Errore durante la configurazione dell'indovinello: " + e.getMessage());
                }
            } else if ("oggetto".equalsIgnoreCase(tipoScelta)) {
                try {
                    scenario.getOggetto(scenario.getId());
                    logger.debug("Oggetto configurato per lo scenario: {}", scenario.getId());
                } catch (Exception e) {
                    logger.error("Errore durante la configurazione dell'oggetto.", e);
                    throw new Exception("Errore durante la configurazione dell'oggetto: " + e.getMessage());
                }
            }
        }

        try {
            Map<String, Object> savedStoryData = storiaModel.getSavedStory(giocatore.getUid(), idStoria);
            if (savedStoryData != null && !savedStoryData.isEmpty()) {
                String scenarioId = (String) savedStoryData.get("scenarioId");
                this.scenarioCorrente = storia.getScenarioById(scenarioId);

                String[] inventory = (String[]) savedStoryData.get("inventory");
                if (inventory != null && inventory.length != 0) {
                    for (String oggetto : inventory) {
                        this.inventario.aggiungiOggetto(new Oggetto(oggetto));
                    }
                }
                logger.debug("Partita ripresa da scenario: {}", scenarioId);

                deleteSavedStory(giocatore.getUid(), idStoria);
            } else {
                this.scenarioCorrente = storia.getPrimoScenario();
                logger.debug("Scenario corrente impostato con ID: {}", scenarioCorrente.getId());
            }
        } catch (Exception e) {
            logger.error("Errore durante l'impostazione dello scenario corrente.", e);
            throw new Exception("Errore durante l'impostazione dello scenario corrente: " + e.getMessage());
        }
    }

    /**
     * Gioca un turno della partita in base alla risposta fornita.
     * @param risposta La risposta dell'utente.
     * @return La descrizione dello scenario successivo o un messaggio di fine gioco.
     */
    public String play(String risposta) {
        if (!stato) {
            logger.debug("La partita è già terminata.");
            return "La partita è già terminata.";
        }

        if (!scenarioCorrente.hasOggetto()) {
            this.inventario.aggiungiOggetto(new Oggetto(scenarioCorrente.getOggetto()));
            logger.debug("Oggetto aggiunto all'inventario: {}", scenarioCorrente.getOggetto());
        }

        if (!scenarioCorrente.hasScelte()) {
            terminaPartita();
            logger.debug("Fine della storia raggiunta.");
            return "Hai raggiunto la fine della storia!";
        }

        Scelta scelta = scenarioCorrente.getScelte();
        String prossimoScenarioId = scelta.esegui(this, risposta);
        logger.debug("Prossimo scenario ID: {}", prossimoScenarioId);

        scenarioCorrente = storia.getScenarioById(prossimoScenarioId);

        if (!scenarioCorrente.hasScelte()) {
            terminaPartita();
            return scenarioCorrente.getDescrizione() + " (Fine della storia)";
        }

        return scenarioCorrente.getDescrizione();
    }
    
    public void saveStory(String userId, String storyId, String scenarioId) throws Exception {
        String[] inventory = getOggetti();
        logger.debug("Salvataggio della storia per userId: {}, storyId: {}, scenarioId: {}, inventario: {}", userId,
                storyId, scenarioId, inventory);
        storiaModel.saveStory(userId, storyId, scenarioId, inventory);
    }

    public void deleteSavedStory(String userid, String idStoria) throws Exception {
        storiaModel.deleteSavedStory(userid, idStoria);
    }

    public void avanzaScenario(Scenario nuovoScenario) {
        this.scenarioCorrente = nuovoScenario;
    }

    public void aggiungiOggetto(Oggetto oggetto) {
        inventario.aggiungiOggetto(oggetto);
    }

    public boolean verificaOggettoPosseduto(String nomeOggetto) {
        return inventario.haOggetto(nomeOggetto);
    }

    public String[] getOggetti() {
        String[] oggetti = inventario.getNomiOggetti();
        if (oggetti == null || oggetti.length == 0) {
            return new String[0];
        }
        return oggetti;
    } 

    public Scenario getScenarioCorrente() {
        return scenarioCorrente;
    }

    public void terminaPartita() {
        this.stato = false;
    }

    public void setGiocatore(Giocatore giocatore) {
        this.giocatore = giocatore;
    }
}
