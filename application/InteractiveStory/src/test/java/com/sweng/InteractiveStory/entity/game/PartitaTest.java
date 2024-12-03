package com.sweng.InteractiveStory.entity.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sweng.InteractiveStory.entity.option.Scelta;
import com.sweng.InteractiveStory.entity.user.Giocatore;
import com.sweng.InteractiveStory.entity.utility.Oggetto;
import com.sweng.InteractiveStory.model.*;

import java.util.Map;

class PartitaTest {
    private Partita partita;
    private Giocatore giocatore;
    private StoriaModel storiaModel;
    private ScenarioModel scenarioModel;
    private SceltaIndovinelloModel sceltaIndovinelloModel;
    private SceltaOggettoModel sceltaOggettoModel;
    private Storia storiaMock;
    private Scenario scenarioMock;

    @BeforeEach
    void setUp() {
        giocatore = new Giocatore("id","Player1","email");
        partita = new Partita(giocatore);

        storiaModel = mock(StoriaModel.class);
        scenarioModel = mock(ScenarioModel.class);
        sceltaIndovinelloModel = mock(SceltaIndovinelloModel.class);
        sceltaOggettoModel = mock(SceltaOggettoModel.class);

        storiaMock = mock(Storia.class);
        scenarioMock = mock(Scenario.class);
    }

    @Test
    void testSetup_ValidScenario() throws Exception {
        // Mock della storia
        when(storiaModel.getStoria("1")).thenReturn(Map.of(
            "id", "1",
            "titolo", "Titolo Storia",
            "descrizione", "Descrizione Storia",
            "idscrittore", "Autore"
        ));
        when(storiaModel.getScenari("1")).thenReturn(new String[]{"1", "2"});
        when(scenarioModel.getScenario("1", "1")).thenReturn(Map.of(
            "id", "1",
            "descrizione", "Descrizione Scenario 1",
            "order", "1",
            "tipoScelta", "indovinello",
            "oggetto", "Oggetto1"
        ));
        when(scenarioModel.getScenario("1", "2")).thenReturn(Map.of(
            "id", "2",
            "descrizione", "Descrizione Scenario 2",
            "order", "2",
            "tipoScelta", "oggetto",
            "oggetto", "Oggetto2"
        ));

        // Mock del primo scenario
        when(storiaMock.getPrimoScenario()).thenReturn(scenarioMock);
        when(scenarioMock.getId()).thenReturn("1");

        // Esegui il setup
        partita.setup("1", storiaModel, scenarioModel, sceltaIndovinelloModel, sceltaOggettoModel);

        // Verifica lo scenario corrente
        assertNotNull(partita.getScenarioCorrente());
        assertEquals("1", partita.getScenarioCorrente().getId());
    }

    @Test
    void testPlay_EndScenario() {
        // Mock dello scenario corrente senza scelte
        when(scenarioMock.hasScelte()).thenReturn(false);
        when(scenarioMock.getDescrizione()).thenReturn("Descrizione Finale");
        partita.avanzaScenario(scenarioMock);

        // Esegui il gioco
        String risultato = partita.play("Qualsiasi risposta");

        // Verifica che il gioco termini
        assertEquals("Descrizione Finale (Fine della storia)", risultato);
        assertFalse(partita.getStato());
        assertNotNull(partita.getDataFine());
    }

    @Test
    void testPlay_ProssimoScenario() {
        // Mock dello scenario corrente con scelte
        Scelta sceltaMock = mock(Scelta.class);
        when(scenarioMock.hasScelte()).thenReturn(true);
        when(scenarioMock.getScelte()).thenReturn(sceltaMock);
        when(sceltaMock.esegui(partita, "Risposta Corretta")).thenReturn("2");
        when(scenarioMock.getDescrizione()).thenReturn("Scenario Iniziale");
        partita.avanzaScenario(scenarioMock);

        // Mock del prossimo scenario
        Scenario prossimoScenarioMock = mock(Scenario.class);
        when(storiaMock.getScenarioById("2")).thenReturn(prossimoScenarioMock);
        when(prossimoScenarioMock.getDescrizione()).thenReturn("Scenario Successivo");

        // Esegui il gioco
        String risultato = partita.play("Risposta Corretta");

        // Verifica lo scenario corrente
        assertEquals("Scenario Successivo", risultato);
        assertNotNull(partita.getScenarioCorrente());
    }

    @Test
    void testVerificaOggettoPosseduto() {
        // Mock dell'inventario
        Oggetto oggetto = new Oggetto("Spada Magica");
        partita.aggiungiOggetto(oggetto);

        // Verifica che l'oggetto sia posseduto
        assertTrue(partita.verificaOggettoPosseduto("Spada Magica"));

        // Verifica che un oggetto non presente non venga trovato
        assertFalse(partita.verificaOggettoPosseduto("Pietra Misteriosa"));
    }

    @Test
    void testTerminaPartita() {
        // Termina la partita
        partita.terminaPartita();

        // Verifica lo stato della partita
        assertFalse(partita.getStato());
        assertNotNull(partita.getDataFine());
    }
}
