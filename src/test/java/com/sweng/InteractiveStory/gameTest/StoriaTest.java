package com.sweng.InteractiveStory.gameTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sweng.InteractiveStory.model.ScenarioModel;
import com.sweng.InteractiveStory.model.StoriaModel;
import com.sweng.InteractiveStory.entity.game.Scenario;
import com.sweng.InteractiveStory.entity.game.Storia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

class StoriaTest {

    private Storia storia;
    private StoriaModel mockStoriaAdapter;
    private ScenarioAdapter mockScenarioAdapter;

    @BeforeEach
    void setUp() {
        // Crea un'istanza di Storia vuota
        storia = new Storia();

        // Mock degli adapter
        mockStoriaAdapter = mock(StoriaAdapter.class);
        mockScenarioAdapter = mock(ScenarioAdapter.class);

        // Inizializza la lista di scenari nella classe Storia
        storia.addScenario(new Scenario("storia1", "sc1", "Scenario 1", 1, "indovinello", null));
    }

    @Test
    void testGetStoriaSuccess() throws Exception {
        // Configura il mock per restituire i dettagli della storia
        when(mockStoriaAdapter.getStoria("storia1")).thenReturn(Map.of(
            "id", "storia1",
            "titolo", "La Grande Avventura",
            "descrizione", "Una storia avvincente.",
            "idscrittore", "scrittore1"
        ));

        // Esegui il metodo
        storia.getStoria("storia1", mockStoriaAdapter);

        // Verifica i risultati
        assertEquals("storia1", storia.getId());
        assertEquals("La Grande Avventura", storia.getTitolo());
        assertEquals("Una storia avvincente.", storia.getDescrizione());
    }

    @Test
    void testGetStoriaFailure() throws Exception {
        // Configura il mock per lanciare un'eccezione
        when(mockStoriaAdapter.getStoria("storia1")).thenThrow(new Exception("Errore durante il caricamento della storia"));

        // Verifica che venga sollevata un'eccezione
        Exception exception = assertThrows(Exception.class, () -> storia.getStoria("storia1", mockStoriaAdapter));
        assertEquals("Errore durante il caricamento della storia", exception.getMessage());
    }

    @Test
    void testGetScenariSuccess() throws Exception {
        // Configura il mock per restituire gli ID degli scenari
        when(mockStoriaAdapter.getScenari("storia1")).thenReturn(new String[]{"sc1", "sc2"});

        // Configura il mock per restituire i dettagli di ogni scenario
        when(mockScenarioAdapter.getScenario("sc1")).thenReturn(Map.of(
            "id", "sc1",
            "descrizione", "Scenario 1",
            "order", "1",
            "tipoScelta", "indovinello",
            "oggetto", null
        ));
        when(mockScenarioAdapter.getScenario("sc2")).thenReturn(Map.of(
            "id", "sc2",
            "descrizione", "Scenario 2",
            "order", "2",
            "tipoScelta", "oggetto",
            "oggetto", "Chiave dorata"
        ));

        // Esegui il metodo
        storia.getScenari("storia1", mockStoriaAdapter, mockScenarioAdapter);

        // Verifica i risultati
        List<Scenario> scenari = storia.getScenariList();
        assertEquals(2, scenari.size());

        Scenario scenario1 = scenari.get(0);
        assertEquals("sc1", scenario1.getId());
        assertEquals("Scenario 1", scenario1.getDescrizione());
        assertEquals(1, scenario1.getOrderNumber());
        assertEquals("indovinello", scenario1.getTipoScelta());

        Scenario scenario2 = scenari.get(1);
        assertEquals("sc2", scenario2.getId());
        assertEquals("Scenario 2", scenario2.getDescrizione());
        assertEquals(2, scenario2.getOrderNumber());
        assertEquals("oggetto", scenario2.getTipoScelta());
        assertEquals("Chiave dorata", scenario2.getOggetto());
    }

    @Test
    void testGetPrimoScenario() {
        // Aggiungi uno scenario con ordine 1
        Scenario primoScenario = storia.getPrimoScenario();

        // Verifica che il primo scenario sia corretto
        assertNotNull(primoScenario);
        assertEquals(1, primoScenario.getOrderNumber());
    }

    @Test
    void testGetPrimoScenarioFailure() {
        // Rimuovi tutti gli scenari dalla lista
        storia.getScenariList().clear();

        // Verifica che venga sollevata un'eccezione
        Exception exception = assertThrows(IllegalArgumentException.class, () -> storia.getPrimoScenario());
        assertEquals("Primo scenario non trovato.", exception.getMessage());
    }

    @Test
    void testGetScenarioByIdSuccess() {
        // Recupera uno scenario per ID
        Scenario scenario = storia.getScenarioById("sc1");

        // Verifica i risultati
        assertNotNull(scenario);
        assertEquals("sc1", scenario.getId());
    }

    @Test
    void testGetScenarioByIdFailure() {
        // Verifica che venga sollevata un'eccezione per un ID inesistente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> storia.getScenarioById("sc99"));
        assertEquals("Scenario con ID sc99 non trovato.", exception.getMessage());
    }
}

