package com.sweng.InteractiveStory.entity.option;

import com.sweng.InteractiveStory.entity.game.Partita;

public class SceltaOggetto implements Scelta {
    private String nomeOggettoRichiesto;
    private String scenarioConOggetto;
    private String scenarioSenzaOggetto;

    public SceltaOggetto(String nomeOggettoRichiesto, String scenarioConOggetto, String scenarioSenzaOggetto) {
        this.nomeOggettoRichiesto = nomeOggettoRichiesto;
        this.scenarioConOggetto = scenarioConOggetto;
        this.scenarioSenzaOggetto = scenarioSenzaOggetto;
    }

    public String esegui(Partita partita, String risposta) {
        if (partita.verificaOggettoPosseduto(nomeOggettoRichiesto)) {
            return scenarioConOggetto;
        }
        return scenarioSenzaOggetto;
    }

    @Override
    public String testoDaMostrare() {
        return nomeOggettoRichiesto;
    }

    @Override
    public String getProssimoScenarioCorretto() {
        return scenarioConOggetto;
    }

    @Override
    public String getProssimoScenarioErrato() {
        return scenarioSenzaOggetto;
    }

    public String getNomeOggetto() {
        return nomeOggettoRichiesto;
    }

    public String getIdScenarioConOggetto() {
        return scenarioConOggetto;
    }

    public String getIdScenarioSenzaOggetto() {
        return scenarioSenzaOggetto;
    }
}
