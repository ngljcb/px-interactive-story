package com.sweng.InteractiveStory.entity.game; 

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sweng.InteractiveStory.model.ScenarioModel;
import com.sweng.InteractiveStory.model.StoriaModel;

class StoriaTest {

    private Storia storia;
    private StoriaModel mockStoriaModel;
    private ScenarioModel mockScenarioModel;

    @BeforeEach
    void setUp() {
        // Inizializza la classe Storia
        storia = new Storia("storia1", "Titolo di prova", "Descrizione di prova", "scrittore1");

        // Mock dei modelli
        mockStoriaModel = mock(StoriaModel.class);
        mockScenarioModel = mock(ScenarioModel.class);
    }

    @Test
    void testGetStoria_PopolaProprietàCorrettamente() throws Exception {
        // Mock dei dati della storia
        Map<String, String> datiStoria = Map.of(
            "id", "storia1",
            "titolo", "Titolo mock",
            "descrizione", "Descrizione mock",
            "idscrittore", "scrittoreMock"
        );

        when(mockStoriaModel.getStoria("storia1")).thenReturn(datiStoria);

        // Esegui il metodo
        storia.getStoria("storia1", mockStoriaModel);

        // Verifica che i dati siano stati popolati correttamente
        assertEquals("storia1", storia.getId());
        assertEquals("Titolo mock", storia.getTitolo());
        assertEquals("Descrizione mock", storia.getDescrizione());
    }

    @Test
    void testGetStoria_ErroreNelRecupero() throws Exception {
        // Simula un'eccezione durante il recupero
        when(mockStoriaModel.getStoria("storia1"))
            .thenThrow(new RuntimeException("Errore nel recupero della storia"));

        // Verifica che venga lanciata un'eccezione
        Exception exception = assertThrows(Exception.class, () -> {
            storia.getStoria("storia1", mockStoriaModel);
        });

        assertTrue(exception.getMessage().contains("Errore nel recupero della storia"));
    }

    @Test
    void testGetScenari_PopolaListaScenari() throws Exception {
        // Mock degli ID degli scenari
        String[] idScenari = { "scenario1", "scenario2" };
        when(mockStoriaModel.getScenari("storia1")).thenReturn(idScenari);

        // Mock dei dati degli scenari
        Map<String, String> datiScenario1 = Map.of(
            "id", "scenario1",
            "descrizione", "Descrizione scenario 1",
            "order", "1",
            "tipoScelta", "indovinello",
            "oggetto", "Chiave"
        );
        Map<String, String> datiScenario2 = Map.of(
            "id", "scenario2",
            "descrizione", "Descrizione scenario 2",
            "order", "2",
            "tipoScelta", "oggetto",
            "oggetto", "Pozione"
        );

        when(mockScenarioModel.getScenario("storia1", "scenario1")).thenReturn(datiScenario1);
        when(mockScenarioModel.getScenario("storia1", "scenario2")).thenReturn(datiScenario2);

        // Esegui il metodo
        storia.getScenari("storia1", mockStoriaModel, mockScenarioModel);

        // Verifica che la lista degli scenari sia stata popolata correttamente
        List<Scenario> scenari = storia.getScenariList();
        assertEquals(2, scenari.size());

        Scenario scenario1 = scenari.get(0);
        assertEquals("scenario1", scenario1.getId());
        assertEquals("Descrizione scenario 1", scenario1.getDescrizione());
        assertEquals(1, scenario1.getOrderNumber());

        Scenario scenario2 = scenari.get(1);
        assertEquals("scenario2", scenario2.getId());
        assertEquals("Descrizione scenario 2", scenario2.getDescrizione());
        assertEquals(2, scenario2.getOrderNumber());
    }

    @Test
    void testGetScenari_ErroreNelRecupero() throws Exception {
        // Simula un'eccezione durante il recupero degli ID degli scenari
        when(mockStoriaModel.getScenari("storia1"))
            .thenThrow(new RuntimeException("Errore nel recupero degli ID degli scenari"));

        // Verifica che venga lanciata un'eccezione
        Exception exception = assertThrows(Exception.class, () -> {
            storia.getScenari("storia1", mockStoriaModel, mockScenarioModel);
        });

        assertTrue(exception.getMessage().contains("Errore nel recupero degli ID degli scenari"));
    }

    @Test
    void testGetPrimoScenario_RitornaScenarioConOrdine1() throws Exception {
        // Crea un elenco di scenari
        Scenario scenario1 = new Scenario("storia1", "scenario1", "Descrizione 1", 1, "indovinello", "Chiave");
        Scenario scenario2 = new Scenario("storia1", "scenario2", "Descrizione 2", 2, "oggetto", "Pozione");
        storia.addScenario(scenario1);
        storia.addScenario(scenario2);

        // Verifica che il primo scenario sia corretto
        Scenario primoScenario = storia.getPrimoScenario();
        assertEquals("scenario1", primoScenario.getId());
        assertEquals(1, primoScenario.getOrderNumber());
    }

    @Test
    void testGetPrimoScenario_NonTrovato() {
        // Verifica che venga lanciata un'eccezione se non esiste uno scenario con ordine 1
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storia.getPrimoScenario();
        });

        assertEquals("Primo scenario non trovato.", exception.getMessage());
    }

    @Test
    void testGetScenarioById_TrovaScenario() throws Exception {
        // Crea un elenco di scenari
        Scenario scenario = new Scenario("storia1", "scenario1", "Descrizione 1", 1, "indovinello", "Chiave");
        storia.addScenario(scenario);

        // Verifica che lo scenario venga trovato
        Scenario trovato = storia.getScenarioById("scenario1");
        assertEquals("scenario1", trovato.getId());
    }

    @Test
    void testGetScenarioById_NonTrovato() {
        // Verifica che venga lanciata un'eccezione se lo scenario non viene trovato
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storia.getScenarioById("scenarioNonEsistente");
        });

        assertEquals("Scenario con ID scenarioNonEsistente non trovato.", exception.getMessage());
    }
}
