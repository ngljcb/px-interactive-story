package com.sweng.InteractiveStory.gameTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import com.sweng.InteractiveStory.model.*;
import com.sweng.InteractiveStory.entity.game.Partita;
import com.sweng.InteractiveStory.entity.game.Scenario;
import com.sweng.InteractiveStory.entity.game.Storia;
import com.sweng.InteractiveStory.entity.utility.*;
import com.sweng.InteractiveStory.entity.decisione.Scelta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartitaTest {

    private Partita partita;
    private StoriaAdapter mockStoriaAdapter;
    private ScenarioAdapter mockScenarioAdapter;
    private SceltaIndovinelloModel mockSceltaIndovinelloAdapter;
    private SceltaOggettoAdapter mockSceltaOggettoAdapter;
    private Storia mockStoria;
    private Scenario mockScenario;

    @BeforeEach
    void setUp() {
        // Crea una nuova istanza di Partita
        partita = new Partita();

        // Mock delle dipendenze
        mockStoriaAdapter = mock(StoriaAdapter.class);
        mockScenarioAdapter = mock(ScenarioAdapter.class);
        mockSceltaIndovinelloAdapter = mock(SceltaIndovinelloModel.class);
        mockSceltaOggettoAdapter = mock(SceltaOggettoAdapter.class);
        mockStoria = mock(Storia.class);
        mockScenario = mock(Scenario.class);
    }

    @Test
    void testSetupSuccess() throws Exception {
        // Configura il comportamento dei mock
        when(mockStoriaAdapter.getTitolo()).thenReturn("Titolo di prova");
        when(mockStoria.getPrimoScenario()).thenReturn(mockScenario);
        when(mockScenario.getId()).thenReturn("sc1");
        when(mockScenario.getTipoScelta()).thenReturn("oggetto");
        when(mockStoria.getScenariList()).thenReturn(List.of(mockScenario));

        // Esegui il metodo setup
        assertDoesNotThrow(() -> 
            partita.setup("storiaId", mockStoriaAdapter, mockScenarioAdapter, mockSceltaIndovinelloAdapter, mockSceltaOggettoAdapter)
        );

        // Verifica lo stato della partita
        assertNotNull(partita.getScenarioCorrente());
        assertEquals("sc1", partita.getScenarioCorrente().getId());
        verify(mockScenarioAdapter, times(1)).getOggetto(anyString(), eq(mockSceltaOggettoAdapter));
    }

    @Test
    void testSetupWithInvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            partita.setup(null, mockStoriaAdapter, mockScenarioAdapter, mockSceltaIndovinelloAdapter, mockSceltaOggettoAdapter)
        );

        assertEquals("ID della storia non valido o nullo.", exception.getMessage());
    }

    @Test
    void testPlayGameEnds() {
        // Configura lo scenario senza scelte
        when(mockScenario.hasScelte()).thenReturn(false);
        partita.avanzaScenario(mockScenario);

        String result = partita.play("qualsiasi risposta");

        assertEquals("Hai raggiunto la fine della storia!", result);
        assertFalse(partita.getScenarioCorrente().hasScelte());
    }
}

