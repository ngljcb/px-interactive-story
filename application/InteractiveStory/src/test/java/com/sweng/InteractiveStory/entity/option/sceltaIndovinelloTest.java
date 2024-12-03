package com.sweng.InteractiveStory.entity.option;

import com.sweng.InteractiveStory.entity.game.Partita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SceltaIndovinelloTest {

    private SceltaIndovinello sceltaIndovinello;

    @Mock
    private Indovinello mockIndovinello;

    @Mock
    private Partita mockPartita;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configura il mock di Indovinello
        when(mockIndovinello.getTesto()).thenReturn("Qual è il numero primo più piccolo?");
        
        // Inizializza l'oggetto di test
        sceltaIndovinello = new SceltaIndovinello(mockIndovinello, "scenarioCorretto", "scenarioErrato");
    }

    @Test
    void testEsegui_RispostaCorretta() {
        // Configura il mock per restituire true quando la risposta è corretta
        when(mockIndovinello.verificaRisposta("2")).thenReturn(true);

        // Esegui il metodo
        String risultato = sceltaIndovinello.esegui(mockPartita, "2");

        // Verifica che venga restituito lo scenario corretto
        assertEquals("scenarioCorretto", risultato);

        // Verifica che il metodo verificaRisposta sia stato chiamato con la risposta fornita
        verify(mockIndovinello).verificaRisposta("2");
    }

    @Test
    void testEsegui_RispostaErrata() {
        // Configura il mock per restituire false quando la risposta è errata
        when(mockIndovinello.verificaRisposta("5")).thenReturn(false);

        // Esegui il metodo
        String risultato = sceltaIndovinello.esegui(mockPartita, "5");

        // Verifica che venga restituito lo scenario errato
        assertEquals("scenarioErrato", risultato);

        // Verifica che il metodo verificaRisposta sia stato chiamato con la risposta fornita
        verify(mockIndovinello).verificaRisposta("5");
    }

    @Test
    void testTestoDaMostrare() {
        // Esegui il metodo
        String testo = sceltaIndovinello.testoDaMostrare();

        // Verifica che il testo mostrato sia quello fornito dall'indovinello
        assertEquals("Qual è il numero primo più piccolo?", testo);

        // Verifica che il metodo getTesto sia stato chiamato
        verify(mockIndovinello).getTesto();
    }
}
