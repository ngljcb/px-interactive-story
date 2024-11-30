package com.sweng.InteractiveStory.entity.option;

public class IndovinelloNumerico implements Indovinello {
    private String testo;
    private int risposta;

    public IndovinelloNumerico(String testo, int risposta) {
        this.testo = testo;
        this.risposta = risposta;
    }

    @Override
    public String getTesto() {
        return testo;
    }

    @Override
    public boolean verificaRisposta(String risposta) {
        try {
            return Integer.parseInt(risposta) == this.risposta;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
