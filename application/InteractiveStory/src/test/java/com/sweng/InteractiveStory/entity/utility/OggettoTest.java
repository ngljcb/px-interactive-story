package com.sweng.InteractiveStory.entity.utility;

import org.junit.jupiter.api.Test;

import com.sweng.InteractiveStory.entity.utility.Oggetto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OggettoTest {

    @Test
    void testGetNome() {
        // Arrange
        String expectedNome = "Spada";
        Oggetto oggetto = new Oggetto(expectedNome);

        // Act
        String actualNome = oggetto.getNome();

        // Assert
        assertEquals(expectedNome, actualNome, "Il nome restituito non corrisponde a quello assegnato.");
    }
}
