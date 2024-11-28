package com.sweng.InteractiveStory.entity.user;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class GiocatoreTest {

    @Test
    void testCostruttoreConParametri() {
        Giocatore giocatore = new Giocatore("1", "Mario Rossi", "mario.rossi@example.com");

        assertEquals("1", giocatore.getUid());
        assertEquals("Mario Rossi", giocatore.getNome());
        assertEquals("mario.rossi@example.com", giocatore.getEmail());
    }

    @Test
    void testCostruttoreSenzaParametri() {
        Giocatore giocatore = new Giocatore();

        assertNull(giocatore.getUid());
        assertNull(giocatore.getNome());
        assertNull(giocatore.getEmail());
    }

    @Test
    void testSetterGetterUid() {
        Giocatore giocatore = new Giocatore();
        giocatore.setUid("2");

        assertEquals("2", giocatore.getUid());
    }

    @Test
    void testSetterGetterNome() {
        Giocatore giocatore = new Giocatore();
        giocatore.setNome("Luigi Verdi");

        assertEquals("Luigi Verdi", giocatore.getNome());
    }

    @Test
    void testSetterGetterEmail() {
        Giocatore giocatore = new Giocatore();
        giocatore.setEmail("luigi.verdi@example.com");

        assertEquals("luigi.verdi@example.com", giocatore.getEmail());
    }
}

