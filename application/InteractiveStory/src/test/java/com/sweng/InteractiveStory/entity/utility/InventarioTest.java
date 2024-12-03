package com.sweng.InteractiveStory.entity.utility;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventarioTest {

    @Test
    void testInventarioIsEmptyOnInitialization() {
        Inventario inventario = new Inventario();
        assertFalse(inventario.haOggetto("TestOggetto"), "L'inventario dovrebbe essere vuoto al momento dell'inizializzazione.");
    }

    @Test
    void testAggiungiOggetto() {
        Inventario inventario = new Inventario();
        Oggetto oggetto = new Oggetto("Spada");
        inventario.aggiungiOggetto(oggetto);
        assertTrue(inventario.haOggetto("Spada"), "L'oggetto 'Spada' dovrebbe essere presente nell'inventario dopo l'aggiunta.");
    }

    @Test
    void testHaOggetto() {
        Inventario inventario = new Inventario();
        Oggetto oggetto = new Oggetto("Pozione");
        inventario.aggiungiOggetto(oggetto);
        assertTrue(inventario.haOggetto("Pozione"), "L'inventario dovrebbe contenere 'Pozione'.");
        assertFalse(inventario.haOggetto("Spada"), "L'inventario non dovrebbe contenere 'Spada'.");
    }

    @Test
    void testEliminaOggetto() {
        Inventario inventario = new Inventario();
        Oggetto oggetto = new Oggetto("Scudo");
        inventario.aggiungiOggetto(oggetto);
        inventario.eliminaOggetto(oggetto);
        assertFalse(inventario.haOggetto("Scudo"), "L'oggetto 'Scudo' dovrebbe essere stato rimosso dall'inventario.");
    }
}
