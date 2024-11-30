package com.sweng.InteractiveStory.entity.utility;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private List<Oggetto> oggetti;

    public Inventario() {
        oggetti = new ArrayList<>();
    }

    public boolean haOggetto(String nome) {
        return oggetti.stream().anyMatch(o -> o.getNome().equals(nome));
    }

    public void aggiungiOggetto(Oggetto oggetto) {
        oggetti.add(oggetto);
    }

    public void eliminaOggetto(Oggetto oggetto) {
        oggetti.remove(oggetto);
    }
}
