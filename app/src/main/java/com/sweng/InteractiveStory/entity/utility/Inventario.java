package com.sweng.InteractiveStory.entity.utility;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private List<Oggetto> oggetti;

    /**
     * Costruttore per inizializzare l'inventario come una lista vuota di oggetti.
     */
    public Inventario() {
        oggetti = new ArrayList<>();
    }

    /**
     * Verifica se un oggetto con un determinato nome è presente nell'inventario.
     *
     * @param nome Il nome dell'oggetto da cercare.
     * @return true se l'oggetto è presente, false altrimenti.
     */
    public boolean haOggetto(String nome) {
        return oggetti.stream().anyMatch(o -> o.getNome().equals(nome));
    }

    /**
     * Aggiunge un oggetto all'inventario.
     *
     * @param oggetto L'oggetto da aggiungere.
     */
    public void aggiungiOggetto(Oggetto oggetto) {
        oggetti.add(oggetto);
    }

    /**
     * Rimuove un oggetto dall'inventario.
     *
     * @param oggetto L'oggetto da rimuovere.
     */
    public void eliminaOggetto(Oggetto oggetto) {
        oggetti.remove(oggetto);
    }

    /**
     * Restituisce un array di stringhe contenente i nomi degli oggetti nell'inventario.
     *
     * @return Array di stringhe con i nomi degli oggetti.
     */
    public String[] getNomiOggetti() {
        return oggetti.stream()
                .map(Oggetto::getNome)
                .toArray(String[]::new);
    }
    
    public boolean isEmpty() {
        return oggetti.isEmpty();
    }
}

