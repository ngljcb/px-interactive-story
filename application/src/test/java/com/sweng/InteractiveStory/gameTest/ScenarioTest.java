package com.sweng.InteractiveStory.gameTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sweng.InteractiveStory.adapter.SceltaIndovinelloAdapter;
import com.sweng.InteractiveStory.adapter.SceltaOggettoAdapter;
import com.sweng.InteractiveStory.entity.game.Scenario;
import com.sweng.InteractiveStory.entity.decisione.Scelta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class ScenarioTest {

    private Scenario scenario;
    private SceltaIndovinelloAdapter mockIndovinelloAdapter;
    private SceltaOggettoAdapter mockOggettoAdapter;

    @BeforeEach
    void setUp() {
        // Creazione di un'istanza di Scenario
        scenario = new Scenario("storia1", "sc1", "Descrizione di prova", 1, "indovinello", null);

        // Mock delle dipendenze
        mockIndovinelloAdapter = mock(SceltaIndovinelloAdapter.class);
        mockOggettoAdapter = mock(SceltaOggettoAdapter.class);
    }

    @Test
    void testGetIndovinelloSuccess() throws Exception {
        // Configura il comportamento del mock
        when(mockIndovinelloAdapter.getIndovinello("storia1", "sc1")).thenReturn(Map.of(
            "testo", "Qual è la capitale d'Italia?",
            "risposta", "Roma",
            "prox-scenario-corretto", "sc2",
            "prox-scenario-errato", "sc3"
        ));

        // Esegui il metodo
        scenario.getIndovinello("sc1", mockIndovinelloAdapter);

        // Verifica i risultati
        assertTrue(scenario.hasScelte());
        assertTrue(scenario.getScelte() instanceof SceltaIndovinello);
        SceltaIndovinello scelta = (SceltaIndovinello) scenario.getScelte();
        assertEquals("Roma", scelta.getIndovinello().getRisposta());
        assertEquals("sc2", scelta.getIdScenarioCorretto());
        assertEquals("sc3", scelta.getIdScenarioErrato());
    }

    @Test
    void testGetIndovinelloFailure() throws Exception {
        // Configura il mock per lanciare un'eccezione
        when(mockIndovinelloAdapter.getIndovinello("storia1", "sc1")).thenThrow(new Exception("Errore durante il recupero dell'indovinello"));

        // Verifica che venga sollevata un'eccezione
        Exception exception = assertThrows(Exception.class, () -> scenario.getIndovinello("sc1", mockIndovinelloAdapter));
        assertEquals("Errore durante il recupero dell'indovinello", exception.getMessage());
    }

    @Test
    void testGetOggettoSuccess() throws Exception {
        // Configura il comportamento del mock
        when(mockOggettoAdapter.getOggetto("storia1", "sc1")).thenReturn(Map.of(
            "nome-oggetto", "Chiave dorata",
            "prox-scenario-corretto", "sc2",
            "prox-scenario-errato", "sc3"
        ));

        // Esegui il metodo
        scenario.getOggetto("sc1", mockOggettoAdapter);

        // Verifica i risultati
        assertTrue(scenario.hasScelte());
        assertTrue(scenario.getScelte() instanceof SceltaOggetto);
        SceltaOggetto scelta = (SceltaOggetto) scenario.getScelte();
        assertEquals("Chiave dorata", scelta.getNomeOggetto());
        assertEquals("sc2", scelta.getIdScenarioConOggetto());
        assertEquals("sc3", scelta.getIdScenarioSenzaOggetto());
    }

    @Test
    void testGetOggettoFailure() throws Exception {
        // Configura il mock per lanciare un'eccezione
        when(mockOggettoAdapter.getOggetto("storia1", "sc1")).thenThrow(new Exception("Errore durante il recupero dell'oggetto"));

        // Verifica che venga sollevata un'eccezione
        Exception exception = assertThrows(Exception.class, () -> scenario.getOggetto("sc1", mockOggettoAdapter));
        assertEquals("Errore durante il recupero dell'oggetto", exception.getMessage());
    }

    @Test
    void testGettersAndSetters() {
        // Verifica i getter e setter
        scenario.setDescrizione("Nuova descrizione");
        assertEquals("Nuova descrizione", scenario.getDescrizione());

        scenario.setTipoScelta("oggetto");
        assertEquals("oggetto", scenario.getTipoScelta());

        scenario.setOggetto("Spada magica");
        assertEquals("Spada magica", scenario.getOggetto());
        assertTrue(scenario.hasOggetto());
    }
}

