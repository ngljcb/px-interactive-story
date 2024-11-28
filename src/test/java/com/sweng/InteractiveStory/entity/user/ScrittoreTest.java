package com.sweng.InteractiveStory.entity.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ScrittoreTest {

    @Test
    void testCostruttoreEGetter() {
        // Arrange
        String uid = "12345";
        String nome = "Mario Rossi";
        String email = "mario.rossi@example.com";
        boolean storyAdmin = true;

        // Act
        Scrittore scrittore = new Scrittore(uid, nome, email, storyAdmin);

        // Assert
        assertEquals(uid, scrittore.getUid(), "UID non corrisponde");
        assertEquals(nome, scrittore.getNome(), "Nome non corrisponde");
        assertEquals(email, scrittore.getEmail(), "Email non corrisponde");
        assertTrue(scrittore.isStoryAdmin(), "StoryAdmin dovrebbe essere true");
    }

    @Test
    void testSetterStoryAdmin() {
        // Arrange
        Scrittore scrittore = new Scrittore("12345", "Mario Rossi", "mario.rossi@example.com", false);

        // Act
        scrittore.setStoryAdmin(true);

        // Assert
        assertTrue(scrittore.isStoryAdmin(), "StoryAdmin dovrebbe essere true dopo il set");
    }

    @Test
    void testEreditarieta() {
        // Arrange
        String uid = "54321";
        String nome = "Giulia Verdi";
        String email = "giulia.verdi@example.com";

        // Act
        Scrittore scrittore = new Scrittore(uid, nome, email, false);

        // Assert
        assertTrue(scrittore instanceof Giocatore, "Scrittore dovrebbe essere una sottoclasse di Giocatore");
        assertEquals(uid, scrittore.getUid(), "UID ereditato non corrisponde");
        assertEquals(nome, scrittore.getNome(), "Nome ereditato non corrisponde");
        assertEquals(email, scrittore.getEmail(), "Email ereditata non corrisponde");
    }
}
