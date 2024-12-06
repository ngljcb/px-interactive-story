package com.sweng.InteractiveStory.entity.option;

import org.junit.jupiter.api.Test;

import com.sweng.InteractiveStory.entity.game.Partita;

import static org.junit.jupiter.api.Assertions.*;

class sceltaIndovinelloTest {

    @Test
    void testEseguiRispostaCorretta() {
        // Crea un indovinello con una risposta corretta
        Indovinello indovinello = new IndovinelloTestuale("Qual è il colmo per un matematico?", "Avere una mente quadrata");
        
        // Crea la scelta con indovinello e scenari
        SceltaIndovinello scelta = new SceltaIndovinello(indovinello, "scenario1", "scenario2");

        // Esegui la scelta con la risposta corretta
        String risultato = scelta.esegui(new Partita(), "Avere una mente quadrata");

        // Verifica che il risultato sia lo scenario corretto
        assertEquals("scenario1", risultato);
    }

    @Test
    void testEseguiRispostaErrata() {
        // Crea un indovinello con una risposta corretta
        Indovinello indovinello = new IndovinelloTestuale("Qual è il colmo per un matematico?", "Avere una mente quadrata");
        
        // Crea la scelta con indovinello e scenari
        SceltaIndovinello scelta = new SceltaIndovinello(indovinello, "scenario1", "scenario2");

        // Esegui la scelta con una risposta errata
        String risultato = scelta.esegui(new Partita(), "Non avere una mente quadrata");

        // Verifica che il risultato sia lo scenario errato
        assertEquals("scenario2", risultato);
    }

    @Test
    void testTestoDaMostrare() {
        // Crea un indovinello
        Indovinello indovinello = new IndovinelloTestuale("Qual è il colmo per un matematico?", "Avere una mente quadrata");
        
        // Crea la scelta con l'indovinello
        SceltaIndovinello scelta = new SceltaIndovinello(indovinello, "scenario1", "scenario2");

        // Verifica che il testo da mostrare dell'indovinello sia corretto
        assertEquals("Qual è il colmo per un matematico?", scelta.testoDaMostrare());
    }

    @Test
    void testGetProssimoScenarioCorretto() {
        // Crea un indovinello
        Indovinello indovinello = new IndovinelloTestuale("Qual è il colmo per un matematico?", "Avere una mente quadrata");
        
        // Crea la scelta con l'indovinello
        SceltaIndovinello scelta = new SceltaIndovinello(indovinello, "scenario1", "scenario2");

        // Verifica che il prossimo scenario corretto sia quello giusto
        assertEquals("scenario1", scelta.getProssimoScenarioCorretto());
    }

    @Test
    void testGetProssimoScenarioErrato() {
        // Crea un indovinello
        Indovinello indovinello = new IndovinelloTestuale("Qual è il colmo per un matematico?", "Avere una mente quadrata");
        
        // Crea la scelta con l'indovinello
        SceltaIndovinello scelta = new SceltaIndovinello(indovinello, "scenario1", "scenario2");

        // Verifica che il prossimo scenario errato sia quello giusto
        assertEquals("scenario2", scelta.getProssimoScenarioErrato());
    }
}

