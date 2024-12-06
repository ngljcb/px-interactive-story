package com.sweng.InteractiveStory.entity.option;

import org.junit.jupiter.api.Test;

import com.sweng.InteractiveStory.entity.game.Partita;
import com.sweng.InteractiveStory.entity.utility.Oggetto;

import static org.junit.jupiter.api.Assertions.*;

class sceltaOggettoTest {

    @Test
void testEseguiConOggetto() {
    // Crea una partita
    Partita partita = new Partita();

    // Crea un oggetto "Spada" e aggiungilo alla partita
    Oggetto spada = new Oggetto("Spada");
    partita.aggiungiOggetto(spada);

    // Crea una SceltaOggetto che richiede l'oggetto "Spada"
    SceltaOggetto scelta = new SceltaOggetto("Spada", "scenarioConOggetto", "scenarioSenzaOggetto");

    // Esegui la scelta, passando l'oggetto posseduto
    String prossimoScenario = scelta.esegui(partita, "Spada");

    // Verifica che lo scenario corretto venga restituito
    assertEquals("scenarioConOggetto", prossimoScenario);
}

    @Test
    void testEseguiSenzaOggetto() {
        // Crea una partita senza oggetti
        Partita partita = new Partita();

        // Crea una SceltaOggetto con uno scenario che richiede l'oggetto "Spada"
        SceltaOggetto scelta = new SceltaOggetto("Spada", "scenarioConOggetto", "scenarioSenzaOggetto");

        // Esegui la scelta, passando l'oggetto che non è posseduto
        String prossimoScenario = scelta.esegui(partita, "Spada");

        // Verifica che lo scenario senza oggetto venga restituito
        assertEquals("scenarioSenzaOggetto", prossimoScenario);
    }

    @Test
    void testTestoDaMostrare() {
        // Crea una SceltaOggetto
        SceltaOggetto scelta = new SceltaOggetto("Spada", "scenarioConOggetto", "scenarioSenzaOggetto");

        // Verifica che il testo da mostrare sia il nome dell'oggetto
        assertEquals("Spada", scelta.testoDaMostrare());
    }

    @Test
    void testGetProssimoScenarioCorretto() {
        // Crea una SceltaOggetto
        SceltaOggetto scelta = new SceltaOggetto("Spada", "scenarioConOggetto", "scenarioSenzaOggetto");

        // Verifica che il prossimo scenario corretto sia quello con oggetto
        assertEquals("scenarioConOggetto", scelta.getProssimoScenarioCorretto());
    }

    @Test
    void testGetProssimoScenarioErrato() {
        // Crea una SceltaOggetto
        SceltaOggetto scelta = new SceltaOggetto("Spada", "scenarioConOggetto", "scenarioSenzaOggetto");

        // Verifica che il prossimo scenario errato sia quello senza oggetto
        assertEquals("scenarioSenzaOggetto", scelta.getProssimoScenarioErrato());
    }

    @Test
    void testGetNomeOggetto() {
        // Crea una SceltaOggetto
        SceltaOggetto scelta = new SceltaOggetto("Spada", "scenarioConOggetto", "scenarioSenzaOggetto");

        // Verifica che il nome dell'oggetto richiesto sia "Spada"
        assertEquals("Spada", scelta.getNomeOggetto());
    }

    @Test
    void testGetIdScenarioConOggetto() {
        // Crea una SceltaOggetto
        SceltaOggetto scelta = new SceltaOggetto("Spada", "scenarioConOggetto", "scenarioSenzaOggetto");

        // Verifica che l'ID dello scenario con oggetto sia "scenarioConOggetto"
        assertEquals("scenarioConOggetto", scelta.getIdScenarioConOggetto());
    }

    @Test
    void testGetIdScenarioSenzaOggetto() {
        // Crea una SceltaOggetto
        SceltaOggetto scelta = new SceltaOggetto("Spada", "scenarioConOggetto", "scenarioSenzaOggetto");

        // Verifica che l'ID dello scenario senza oggetto sia "scenarioSenzaOggetto"
        assertEquals("scenarioSenzaOggetto", scelta.getIdScenarioSenzaOggetto());
    }
}

