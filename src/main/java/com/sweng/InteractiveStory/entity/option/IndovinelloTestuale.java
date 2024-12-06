package com.sweng.InteractiveStory.entity.option;

public class IndovinelloTestuale implements Indovinello {
    private String testo;
    private String risposta;

    public IndovinelloTestuale(String testo, String risposta) {
        this.testo = testo;
        this.risposta = risposta.toLowerCase();
    }

    @Override
    public String getTesto() {
        return testo;
    }

    @Override
    public boolean verificaRisposta(String risposta) {
        return risposta.toLowerCase().equals(this.risposta);
    }

    public String getRisposta() {
        return risposta;
    }
}
