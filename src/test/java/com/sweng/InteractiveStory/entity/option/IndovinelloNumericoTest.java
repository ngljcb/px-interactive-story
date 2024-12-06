package com.sweng.InteractiveStory.entity.option;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class IndovinelloNumericoTest {

    @Test
    void testVerificaRisposta() {
        IndovinelloNumerico indovinello = new IndovinelloNumerico("Quanti sono i pianeti nel sistema solare?", 8);
        assertTrue(indovinello.verificaRisposta("8"), "La verifica della risposta corretta dovrebbe restituire true.");
        assertFalse(indovinello.verificaRisposta("9"), "La verifica della risposta errata dovrebbe restituire false.");
        assertEquals(true, indovinello.verificaRisposta("8"));
        assertEquals(false, indovinello.verificaRisposta("9"));
        assertFalse(indovinello.verificaRisposta("ciao"), "La verifica della risposta errata dovrebbe restituire false.");
    }
}
