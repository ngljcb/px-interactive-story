package com.sweng.InteractiveStory.entity.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sweng.InteractiveStory.entity.option.SceltaIndovinello;
import com.sweng.InteractiveStory.entity.option.SceltaOggetto;
import com.sweng.InteractiveStory.entity.option.IndovinelloTestuale;
import com.sweng.InteractiveStory.model.SceltaIndovinelloModel;
import com.sweng.InteractiveStory.model.SceltaOggettoModel;

class ScenarioTest {

    private Scenario scenario;
    private SceltaIndovinelloModel mockSceltaIndovinelloModel;
    private SceltaOggettoModel mockSceltaOggettoModel;

    @BeforeEach
    void setUp() {
        // Inizializza uno scenario di base
        scenario = new Scenario("storia1", "scenario1", "Descrizione test", 1, "indovinello", null);

        // Mock dei modelli
        mockSceltaIndovinelloModel = mock(SceltaIndovinelloModel.class);
        mockSceltaOggettoModel = mock(SceltaOggettoModel.class);
    }

    @Test
    void testGetIndovinello_CreaSceltaIndovinello() throws Exception {
        // Mock dei dati dell'indovinello
        Map<String, String> indovinelloData = Map.of(
            "testo", "Qual è il numero primo più piccolo?",
            "risposta", "2",
            "prox-scenario-corretto", "scenario2",
            "prox-scenario-errato", "scenario3"
        );

        when(mockSceltaIndovinelloModel.getIndovinello("storia1", "scenario1")).thenReturn(indovinelloData);

        // Esegui il metodo
        scenario.getIndovinello("scenario1", mockSceltaIndovinelloModel);

        // Verifica che la scelta sia stata creata correttamente
        assertTrue(scenario.hasScelte());
        assertTrue(scenario.getScelte() instanceof SceltaIndovinello);

        SceltaIndovinello sceltaIndovinello = (SceltaIndovinello) scenario.getScelte();
        IndovinelloTestuale indovinello = (IndovinelloTestuale) sceltaIndovinello.getIndovinello();

        assertEquals("Qual è il numero primo più piccolo?", indovinello.getTesto());
        assertEquals("2", indovinello.getRisposta());
        assertEquals("scenario2", sceltaIndovinello.getProssimoScenarioCorretto());
        assertEquals("scenario3", sceltaIndovinello.getProssimoScenarioErrato());
    }

    @Test
    void testGetIndovinello_ErroreNelRecupero() throws Exception {
        // Simula un'eccezione durante il recupero
        when(mockSceltaIndovinelloModel.getIndovinello("storia1", "scenario1"))
            .thenThrow(new RuntimeException("Errore nel recupero dell'indovinello"));

        // Verifica che venga lanciata un'eccezione
        Exception exception = assertThrows(Exception.class, () -> {
            scenario.getIndovinello("scenario1", mockSceltaIndovinelloModel);
        });

        assertTrue(exception.getMessage().contains("Errore nel recupero dell'indovinello"));
        assertFalse(scenario.hasScelte());
    }

    @Test
    void testGetOggetto_CreaSceltaOggetto() throws Exception {
        // Mock dei dati dell'oggetto
        Map<String, String> oggettoData = Map.of(
            "nome-oggetto", "Chiave dorata",
            "prox-scenario-corretto", "scenario2",
            "prox-scenario-errato", "scenario3"
        );

        when(mockSceltaOggettoModel.getOggetto("storia1", "scenario1")).thenReturn(oggettoData);

        // Esegui il metodo
        scenario.getOggetto("scenario1", mockSceltaOggettoModel);

        // Verifica che la scelta sia stata creata correttamente
        assertTrue(scenario.hasScelte());
        assertTrue(scenario.getScelte() instanceof SceltaOggetto);

        SceltaOggetto sceltaOggetto = (SceltaOggetto) scenario.getScelte();

        assertEquals("Chiave dorata", sceltaOggetto.getNomeOggetto());
        assertEquals("scenario2", sceltaOggetto.getIdScenarioConOggetto());
        assertEquals("scenario3", sceltaOggetto.getIdScenarioSenzaOggetto());
    }

    @Test
    void testGetOggetto_ErroreNelRecupero() throws Exception {
        // Simula un'eccezione durante il recupero
        when(mockSceltaOggettoModel.getOggetto("storia1", "scenario1"))
            .thenThrow(new RuntimeException("Errore nel recupero dell'oggetto"));

        // Verifica che venga lanciata un'eccezione
        Exception exception = assertThrows(Exception.class, () -> {
            scenario.getOggetto("scenario1", mockSceltaOggettoModel);
        });

        assertTrue(exception.getMessage().contains("Errore nel recupero dell'oggetto"));
        assertFalse(scenario.hasScelte());
    }

    @Test
    void testHasOggetto_TrueWhenOggettoIsNotNull() {
        // Verifica che un oggetto esista
        scenario.setOggetto("Chiave magica");
        assertTrue(scenario.hasOggetto());
    }

    @Test
    void testHasOggetto_FalseWhenOggettoIsNull() {
        // Verifica che l'oggetto non esista
        scenario.setOggetto(null);
        assertFalse(scenario.hasOggetto());
    }

    @Test
    void testHasScelte_FalseWhenNoScelte() {
        assertFalse(scenario.hasScelte());
    }

    @Test
    void testHasScelte_TrueWhenScelteIsSet() throws Exception {
        // Mock un'oggetto scelta
        Map<String, String> oggettoData = Map.of(
            "nome-oggetto", "Chiave dorata",
            "prox-scenario-corretto", "scenario2",
            "prox-scenario-errato", "scenario3"
        );

        when(mockSceltaOggettoModel.getOggetto("storia1", "scenario1")).thenReturn(oggettoData);

        // Crea la scelta
        scenario.getOggetto("scenario1", mockSceltaOggettoModel);

        // Verifica
        assertTrue(scenario.hasScelte());
    }
}

