package com.sweng.InteractiveStory.entity.game;

import org.junit.jupiter.api.Test;

import com.sweng.InteractiveStory.entity.user.Giocatore;

import static org.junit.jupiter.api.Assertions.*;

class PartitaTest {

    @Test
    void testPartitaCreataCorrettamente() {
        // Crea un oggetto Giocatore
        Giocatore giocatore = new Giocatore();
        
        // Crea una nuova partita con il giocatore
        Partita partita = new Partita(giocatore);

        // Verifica che lo stato della partita sia attivo (true)
        assertTrue(partita.getStato());

        // Verifica che la data di inizio sia stata impostata correttamente
        assertNotNull(partita.getDataInizio());

        // Verifica che la data di fine sia null
        assertNull(partita.getDataFine());
    }

    @Test
    void testSetupValido() throws Exception {
        // Crea un oggetto Giocatore e una Partita
        Giocatore giocatore = new Giocatore();
        Partita partita = new Partita(giocatore);

        // Crea una storia semplice
        Storia storia = new Storia();
        Scenario scenario1 = new Scenario("1", "1", "descrizione scenario 1", 0, null, null);
        storia.addScenario(scenario1);

        // Esegui la configurazione della partita
        partita.setStoria(storia);
        partita.avanzaScenario(scenario1);

        // Verifica che lo scenario corrente sia impostato correttamente
        assertEquals("descrizione scenario 1", partita.getScenarioCorrente().getDescrizione());
    }

    @Test
    void testPlayFineStoria() {
        // Crea un oggetto Giocatore e una Partita
        Giocatore giocatore = new Giocatore();
        Partita partita = new Partita(giocatore);

        // Crea una storia con uno scenario finale senza scelte
        Storia storia = new Storia();
        Scenario scenarioFinale = new Scenario("2", "Descrizione finale della storia", "fine", 0, null, null);
        storia.addScenario(scenarioFinale);

        // Configura la partita
        partita.setStoria(storia);
        partita.avanzaScenario(scenarioFinale);

        // Simula la fine del gioco
        String risultato = partita.play("fine");

        // Verifica che la partita sia terminata e che il risultato sia corretto
        assertEquals("Hai raggiunto la fine della storia!", risultato);
        assertFalse(partita.getStato());
    }

    @Test
    void testTerminaPartita() {
        // Crea un oggetto Giocatore e una Partita
        Giocatore giocatore = new Giocatore();
        Partita partita = new Partita(giocatore);

        // Termina la partita
        partita.terminaPartita();

        // Verifica che lo stato della partita sia cambiato a false
        assertFalse(partita.getStato());

        // Verifica che la data di fine sia stata impostata
        assertNotNull(partita.getDataFine());
    }
}

