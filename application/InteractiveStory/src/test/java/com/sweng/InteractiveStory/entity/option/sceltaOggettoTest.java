package com.sweng.InteractiveStory.entity.option;

import com.sweng.InteractiveStory.entity.game.Partita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SceltaOggettoTest {

    private SceltaOggetto sceltaOggetto;

    @Mock
    private Partita mockPartita;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Crea un'istanza di SceltaOggetto con dati di test
        sceltaOggetto = new SceltaOggetto("chiave", "scenarioConOggetto", "scenarioSenzaOggetto");
    }

    @Test
    void testEsegui_OggettoPosseduto() {
        // Configura il mock di Partita per restituire true
        when(mockPartita.verificaOggettoPosseduto("chiave")).thenReturn(true);

        // Esegui il metodo
        String risultato = sceltaOggetto.esegui(mockPartita, null);

        // Verifica che il risultato sia lo scenario corretto
        assertEquals("scenarioConOggetto", risultato);

        // Verifica che il metodo verificaOggettoPosseduto sia stato chiamato con il nome dell'oggetto
        verify(mockPartita).verificaOggettoPosseduto("chiave");
    }

    @Test
    void testEsegui_OggettoNonPosseduto() {
        // Configura il mock di Partita per restituire false
        when(mockPartita.verificaOggettoPosseduto("chiave")).thenReturn(false);

        // Esegui il metodo
        String risultato = sceltaOggetto.esegui(mockPartita, null);

        // Verifica che il risultato sia lo scenario errato
        assertEquals("scenarioSenzaOggetto", risultato);

        // Verifica che il metodo verificaOggettoPosseduto sia stato chiamato con il nome dell'oggetto
        verify(mockPartita).verificaOggettoPosseduto("chiave");
    }

    @Test
    void testTestoDaMostrare() {
        // Esegui il metodo
        String testo = sceltaOggetto.testoDaMostrare();

        // Verifica che il testo restituito sia il nome dell'oggetto richiesto
        assertEquals("chiave", testo);
    }
}

