package com.sweng.InteractiveStory.entity.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioTest {

    @Test
    void testScenarioCreazione() {
        // Crea uno scenario di test
        Scenario scenario = new Scenario("storia1", "1", "Descrizione del primo scenario", 1, "indovinello", "Spada");

        // Verifica che i campi siano correttamente assegnati
        assertEquals("1", scenario.getId());
        assertEquals("Descrizione del primo scenario", scenario.getDescrizione());
        assertEquals(1, scenario.getOrderNumber());
        assertEquals("indovinello", scenario.getTipoScelta());
        assertEquals("Spada", scenario.getOggetto());
    }

    @Test
    void testHasScelteSenzaScelte() {
        // Crea uno scenario senza scelte
        Scenario scenario = new Scenario("storia1", "1", "Scenario senza scelte", 1, "oggetto", "Spada");

        // Verifica che lo scenario non abbia scelte
        assertFalse(scenario.hasScelte());
    }

    @Test
    void testHasOggettoSenzaOggetto() {
        // Crea uno scenario senza oggetto
        Scenario scenario = new Scenario("storia1", "2", "Scenario senza oggetto", 1, "indovinello", null);

        // Verifica che lo scenario non abbia oggetto
        assertFalse(scenario.hasOggetto());
    }

    @Test
    void testGetTipoScelta() {
        // Crea uno scenario con tipo di scelta
        Scenario scenario = new Scenario("storia1", "1", "Scenario con tipo di scelta", 1, "oggetto", "Spada");

        // Verifica che il tipo di scelta sia corretto
        assertEquals("oggetto", scenario.getTipoScelta());
    }

    @Test
    void testSetTipoScelta() {
        // Crea uno scenario inizialmente con un tipo di scelta
        Scenario scenario = new Scenario("storia1", "1", "Scenario con tipo di scelta", 1, "oggetto", "Spada");

        // Modifica il tipo di scelta
        scenario.setTipoScelta("indovinello");

        // Verifica che il tipo di scelta sia stato aggiornato
        assertEquals("indovinello", scenario.getTipoScelta());
    }

    @Test
    void testGetDescrizione() {
        // Crea uno scenario con descrizione
        Scenario scenario = new Scenario("storia1", "1", "Descrizione di test", 1, "oggetto", "Spada");

        // Verifica che la descrizione sia corretta
        assertEquals("Descrizione di test", scenario.getDescrizione());
    }

    @Test
    void testSetDescrizione() {
        // Crea uno scenario inizialmente con una descrizione
        Scenario scenario = new Scenario("storia1", "1", "Descrizione iniziale", 1, "oggetto", "Spada");

        // Modifica la descrizione
        scenario.setDescrizione("Nuova descrizione");

        // Verifica che la descrizione sia stata aggiornata
        assertEquals("Nuova descrizione", scenario.getDescrizione());
    }
}
