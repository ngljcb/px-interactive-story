package com.sweng.InteractiveStory.decisioneTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sweng.InteractiveStory.entity.decisione.Opzione;
import com.sweng.InteractiveStory.entity.game.Scenario;
import com.sweng.InteractiveStory.entity.utility.Oggetto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpzioneTest {

    private Opzione opzione;
    private Oggetto mockOggetto;
    private Scenario mockScenarioAttuale;
    private Scenario mockScenarioSuccessivo;

    @BeforeEach
    void setUp() {
        // Crea un oggetto mock
        mockOggetto = mock(Oggetto.class);
        when(mockOggetto.getNome()).thenReturn("Chiave magica");

        // Inizializza un'opzione con l'oggetto mock
        opzione = new Opzione(mockOggetto, "Apri porta segreta");

        // Mock degli scenari
        mockScenarioAttuale = mock(Scenario.class);
        mockScenarioSuccessivo = mock(Scenario.class);
    }

    @Test
    void testGoToScenarioWhenUnlocked() {
        // Sblocca l'opzione
        opzione.sbloccato = true;

        // Verifica che lo scenario successivo venga restituito
        Scenario result = opzione.goToScenario(mockScenarioAttuale, mockScenarioSuccessivo);
        assertEquals(mockScenarioSuccessivo, result);
    }

    @Test
    void testGoToScenarioWhenLocked() {
        // L'opzione non è sbloccata
        opzione.sbloccato = false;

        // Verifica che lo scenario attuale venga restituito
        Scenario result = opzione.goToScenario(mockScenarioAttuale, mockScenarioSuccessivo);
        assertEquals(mockScenarioAttuale, result);
    }

    @Test
    void testVerificaOggettoPossedutoWithCorrectOggetto() {
        // Oggetto posseduto corrisponde all'oggetto dell'opzione
        Oggetto posseduto = mock(Oggetto.class);
        when(posseduto.getNome()).thenReturn("Chiave magica");

        // Verifica l'oggetto posseduto
        opzione.verificaOggettoPosseduto(posseduto);

        // Verifica che l'opzione sia sbloccata
        assertTrue(opzione.sbloccato);
    }

    @Test
    void testVerificaOggettoPossedutoWithIncorrectOggetto() {
        // Oggetto posseduto NON corrisponde all'oggetto dell'opzione
        Oggetto posseduto = mock(Oggetto.class);
        when(posseduto.getNome()).thenReturn("Chiave sbagliata");

        // Verifica l'oggetto posseduto
        opzione.verificaOggettoPosseduto(posseduto);

        // Verifica che l'opzione non sia sbloccata
        assertFalse(opzione.sbloccato);
    }

    @Test
    void testGetNome() {
        // Verifica il nome dell'opzione
        assertEquals("Apri porta segreta", opzione.getNome());
    }
}

