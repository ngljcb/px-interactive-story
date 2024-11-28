package com.sweng.InteractiveStory.entity.option;

import com.sweng.InteractiveStory.entity.game.Partita;

public interface Scelta {
    /**
     * Esegue la logica della scelta e ritorna l'ID del prossimo scenario.
     * @param partita La partita corrente.
     * @param risposta La risposta dell'utente.
     * @return L'ID del prossimo scenario.
     */
    String esegui(Partita partita, String risposta);

    String testoDaMostrare();
    String getProssimoScenarioCorretto();
    String getProssimoScenarioErrato();
}

