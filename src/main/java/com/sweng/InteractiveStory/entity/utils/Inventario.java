package com.sweng.InteractiveStory.entity.utils;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
public class Inventario {
    private List<Oggetto> oggetti;

    public Inventario() {
        oggetti = new ArrayList<Oggetto>();
    }

    public Oggetto cercaOggetto(Oggetto oggetto) { // (optional)
        // todo, itera su la lista per trovare l'oggetto
        return oggetto;
    }

    public void aggiungiOggetto(Oggetto oggetto) {
        oggetti.add(oggetto);
    }

    public void eliminaOggetto(Oggetto oggetto) {
        oggetti.remove(oggetto);
    }
}