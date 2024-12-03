package com.sweng.InteractiveStory.entity.game;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sweng.InteractiveStory.entity.option.Scelta;
import com.sweng.InteractiveStory.entity.user.*;
import com.sweng.InteractiveStory.entity.utility.*;
import com.sweng.InteractiveStory.model.*;

public class Partita {
    private boolean stato;
    private String dataInizio;
    private String dataFine;
    private Giocatore giocatore;
    private Storia storia;
    private Scenario scenarioCorrente;
    private Inventario inventario;

    public Partita(Giocatore giocatore) {
        this.stato = true;
        this.dataInizio = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        this.dataFine = null;
        this.giocatore = giocatore;
        this.inventario = new Inventario();
    }

    public Partita() {
        this.stato = true;
        this.dataInizio = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        this.dataFine = null;
        this.inventario = new Inventario();
    }

    /**
     * Imposta la storia e i suoi scenari.
     */
    public void setup(String idStoria, StoriaModel storiaModel, ScenarioModel scenarioModel, SceltaIndovinelloModel sceltaIndovinelloModel, SceltaOggettoModel sceltaOggettoModel) throws Exception {
        if (idStoria == null || idStoria.isEmpty()) {
            throw new IllegalArgumentException("ID della storia non valido o nullo.");
        }
    
        System.out.println("Inizio setup per la storia con ID: " + idStoria);
    
        // Carica i dettagli della storia
        try {
            this.storia = new Storia();
            storia.getStoria(idStoria, storiaModel);
    
            System.out.println("Storia caricata con successo: " + storia.getTitolo());
        } catch (Exception e) {
            throw new Exception("Errore durante il caricamento della storia: " + e.getMessage());
        }
    
        // Carica gli scenari della storia
        try {
            storia.getScenari(idStoria, storiaModel, scenarioModel);
            System.out.println("Scenari caricati con successo: " + storia.getScenariList().size() + " scenari trovati.");
        } catch (Exception e) {
            throw new Exception("Errore durante il caricamento degli scenari: " + e.getMessage());
        }
    
        // Configura le scelte per ogni scenario
        for (Scenario scenario : storia.getScenariList()) {
            String tipoScelta = scenario.getTipoScelta();
    
            if ("indovinello".equalsIgnoreCase(tipoScelta)) {
                try {
                    scenario.getIndovinello(scenario.getId(), sceltaIndovinelloModel);
                    System.out.println("Indovinello configurato per lo scenario: " + scenario.getId());
                } catch (Exception e) {
                    throw new Exception("Errore durante la configurazione dell'indovinello: " + e.getMessage());
                }
            } else if ("oggetto".equalsIgnoreCase(tipoScelta)) {
                try {
                    scenario.getOggetto(scenario.getId(), sceltaOggettoModel);
                    System.out.println("Oggetto configurato per lo scenario: " + scenario.getId());
                } catch (Exception e) {
                    throw new Exception("Errore durante la configurazione dell'oggetto: " + e.getMessage());
                }
            }
        }
    
        // Imposta lo scenario corrente come il primo
        try {
            this.scenarioCorrente = storia.getPrimoScenario();
            System.out.println("Scenario corrente impostato con ID: " + scenarioCorrente.getId());
        } catch (Exception e) {
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
            return "La partita è già terminata.";
        }

        // Verifica se ci sono oggetti trovati nello scenario corrente
        if (!scenarioCorrente.hasOggetto()) {
            this.inventario.aggiungiOggetto(new Oggetto(scenarioCorrente.getOggetto()));
        }


        // Verifica se ci sono scelte nello scenario corrente
        if (!scenarioCorrente.hasScelte()) {
            terminaPartita();
            return "Hai raggiunto la fine della storia!";
        }

        Scelta scelta = scenarioCorrente.getScelte();

        // Esegui la scelta corrente e ottieni il prossimo scenario
        String prossimoScenarioId = scelta.esegui(this, risposta);
        System.out.println("Prossimo scenario ID: " + prossimoScenarioId);


        // Trova il prossimo scenario nella lista della storia
        scenarioCorrente = storia.getScenarioById(prossimoScenarioId);

        // Controlla se lo scenario successivo è uno scenario finale (senza scelte)
        if (!scenarioCorrente.hasScelte()) {
            terminaPartita();
            return scenarioCorrente.getDescrizione() + " (Fine della storia)";
        }

        return scenarioCorrente.getDescrizione();
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

    public Scenario getScenarioCorrente() {
        return scenarioCorrente;
    }

    public void terminaPartita() {
        this.stato = false;
        this.dataFine = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }

    public void setGiocatore(Giocatore giocatore) {
        this.giocatore = giocatore;
    }
    
    public void setStoria(Storia storia) {
        this.storia = storia;
    }

    public boolean getStato() {
        return stato;
    }

    public String getDataFine() {
        return dataFine;
    }
}
