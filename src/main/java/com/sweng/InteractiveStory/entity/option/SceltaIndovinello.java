package com.sweng.InteractiveStory.entity.option;

import com.sweng.InteractiveStory.entity.game.Partita;

public class SceltaIndovinello implements Scelta {
    private Indovinello indovinello;
    private String scenarioCorretto;
    private String scenarioErrato;

    public SceltaIndovinello(Indovinello indovinello, String scenarioCorretto, String scenarioErrato) {
        this.indovinello = indovinello;
        this.scenarioCorretto = scenarioCorretto;
        this.scenarioErrato = scenarioErrato;
    }

    @Override
    public String esegui(Partita partita, String risposta) {
        if (indovinello.verificaRisposta(risposta)) {
            return scenarioCorretto;
        }
        return scenarioErrato;
    }

    @Override
    public String testoDaMostrare() {
        return indovinello.getTesto();
    }

    @Override
    public String getProssimoScenarioCorretto() {
        return scenarioCorretto;
    }

    @Override
    public String getProssimoScenarioErrato() {
        return scenarioErrato;
    }

    public Indovinello getIndovinello() {
        return indovinello;
    }
}

