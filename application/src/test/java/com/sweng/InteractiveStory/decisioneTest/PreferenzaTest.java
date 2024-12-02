package com.sweng.InteractiveStory.decisioneTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sweng.InteractiveStory.entity.decisione.Opzione;
import com.sweng.InteractiveStory.entity.decisione.Preferenza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PreferenzaTest {

    private Preferenza preferenza;
    private Opzione mockOpzione1;
    private Opzione mockOpzione2;

    @BeforeEach
    void setUp() {
        // Creazione di mock per Opzione
        mockOpzione1 = mock(Opzione.class);
        mockOpzione2 = mock(Opzione.class);

        // Inizializzazione di Preferenza
        preferenza = new Preferenza("Scegli il tuo percorso preferito", mockOpzione1, mockOpzione2);
    }

    @Test
    void testGetDescrizione() {
        // Verifica che la descrizione iniziale sia corretta
        assertEquals("Scegli il tuo percorso preferito", preferenza.getDescrizione());
    }

    @Test
    void testSetDescrizione() {
        // Modifica la descrizione
        preferenza.setDescrizione("Nuova descrizione");

        // Verifica la descrizione aggiornata
        assertEquals("Nuova descrizione", preferenza.getDescrizione());
    }

    @Test
    void testGetOpzione1() {
        // Verifica che l'opzione1 sia quella inizialmente impostata
        assertEquals(mockOpzione1, preferenza.getOpzione1());
    }

    @Test
    void testSetOpzione1() {
        // Crea un'altra opzione mock
        Opzione nuovaOpzione = mock(Opzione.class);

        // Modifica opzione1
        preferenza.setOpzione1(nuovaOpzione);

        // Verifica l'aggiornamento
        assertEquals(nuovaOpzione, preferenza.getOpzione1());
    }

    @Test
    void testGetOpzione2() {
        // Verifica che l'opzione2 sia quella inizialmente impostata
        assertEquals(mockOpzione2, preferenza.getOpzione2());
    }

    @Test
    void testSetOpzione2() {
        // Crea un'altra opzione mock
        Opzione nuovaOpzione = mock(Opzione.class);

        // Modifica opzione2
        preferenza.setOpzione2(nuovaOpzione);

        // Verifica l'aggiornamento
        assertEquals(nuovaOpzione, preferenza.getOpzione2());
    }
}

