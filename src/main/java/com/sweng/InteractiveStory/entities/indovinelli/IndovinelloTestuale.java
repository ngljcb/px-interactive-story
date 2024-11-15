package com.sweng.InteractiveStory.entities.indovinelli;

import com.sweng.InteractiveStory.entities.decisione.Scelta;

public class IndovinelloTestuale implements Indovinello, Scelta{
    private String testo;
    private String risposta;
    private int id;

    public IndovinelloTestuale(String testo, String risposta)
    {
        this.testo = testo;
        this.risposta = risposta; // dev'essere in minuscolo
    }

    //todo implementare metodo goToScenario

    public String getTesto()
    {
        return this.testo;
    }

    public String getRisposta()
    {
        return this.risposta;
    }

    public int getId() {
        return this.id;
    }

    public void setTesto(String testo){
        this.testo = testo;
    }

    public void setRisposta(String risposta) {
        this.risposta = risposta;
    }

    // converte i testi in minuscolo
    private String convertiMinuscolo(String risposta){
        return risposta.toLowerCase();
    }

    // verifica la risposta
    public boolean verificaRisposta(String risposta) {
        return risposta.equals(this.risposta);
    }
}
