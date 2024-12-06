package com.sweng.InteractiveStory.entity.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class StoriaTest {

    @Test
    void testAggiungiScenario() {
        // Crea una storia vuota
        Storia storia = new Storia("1", "La mia storia", "Una bella storia", "scrittore1");

        // Crea uno scenario fittizio
        Scenario scenario = new Scenario("1", "1", "Inizio della storia", 1, "indovinello", "Spada");

        // Aggiungi lo scenario alla storia
        storia.addScenario(scenario);

        // Verifica che lo scenario sia stato aggiunto correttamente
        List<Scenario> scenari = storia.getScenariList();
        assertEquals(1, scenari.size());
        assertEquals("Inizio della storia", scenari.get(0).getDescrizione());
    }

    @Test
    void testGetPrimoScenario() {
        // Crea una storia con due scenari
        Storia storia = new Storia("1", "La mia storia", "Una bella storia", "scrittore1");

        // Crea due scenari fittizi
        Scenario scenario1 = new Scenario("1", "1", "Inizio della storia", 1, "indovinello", "Spada");
        Scenario scenario2 = new Scenario("1", "2", "Secondo scenario", 2, "scelta", "Bastone");

        // Aggiungi gli scenari alla storia
        storia.addScenario(scenario1);
        storia.addScenario(scenario2);

        // Verifica che il primo scenario sia correttamente restituito
        Scenario primoScenario = storia.getPrimoScenario();
        assertEquals("Inizio della storia", primoScenario.getDescrizione());
    }

    @Test
    void testGetScenarioById() {
        // Crea una storia con due scenari
        Storia storia = new Storia("1", "La mia storia", "Una bella storia", "scrittore1");

        // Crea due scenari fittizi
        Scenario scenario1 = new Scenario("1", "1", "Inizio della storia", 1, "indovinello", "Spada");
        Scenario scenario2 = new Scenario("1", "2", "Secondo scenario", 2, "scelta", "Bastone");

        // Aggiungi gli scenari alla storia
        storia.addScenario(scenario1);
        storia.addScenario(scenario2);

        // Verifica che lo scenario venga correttamente restituito usando l'ID
        Scenario scenarioRecuperato = storia.getScenarioById("1");
        assertEquals("Inizio della storia", scenarioRecuperato.getDescrizione());
    }

    @Test
    void testGetScenarioById_NotFound() {
        // Crea una storia vuota
        Storia storia = new Storia("1", "La mia storia", "Una bella storia", "scrittore1");

        // Verifica che venga lanciata un'eccezione se cerchiamo uno scenario che non esiste
        assertThrows(IllegalArgumentException.class, () -> {
            storia.getScenarioById("nonEsistente");
        });
    }
}

