package com.sweng.InteractiveStory.entity.option;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class IndovinelloTestualeTest {

    @Test
    void testVerificaRispostaCorretta() {
        // Arrange
        IndovinelloTestuale indovinello = new IndovinelloTestuale("Qual è il colore del sole?", "giallo");

        // Act & Assert
        assertTrue(indovinello.verificaRisposta("giallo"), "La verifica della risposta corretta dovrebbe restituire true.");
    }

    @Test
    void testVerificaRispostaErrata() {
        // Arrange
        IndovinelloTestuale indovinello = new IndovinelloTestuale("Qual è il colore del sole?", "giallo");

        // Act & Assert
        assertFalse(indovinello.verificaRisposta("rosso"), "La verifica della risposta errata dovrebbe restituire false.");
    }
}
