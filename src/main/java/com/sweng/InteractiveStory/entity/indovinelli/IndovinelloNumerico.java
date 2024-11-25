package com.sweng.InteractiveStory.entity.indovinelli;
import com.sweng.InteractiveStory.entity.decisione.Scelta;
public class IndovinelloNumerico implements Indovinello, Scelta{
    private String testo;
    private int risposta;
    private int id;

    public IndovinelloNumerico(String testo, int risposta)
    {
        this.testo = testo;
        this.risposta = risposta;
    }

    //todo implementare metodo goToScenario

    public String getTesto()
    {
        return this.testo;
    }

    public int getRisposta() {
        return this.risposta;
    }

    public int getId() {
        return this.id;
    }

    public void setTesto(String testo){
        this.testo = testo;
    }

    public void setRisposta(int risposta) {
        this.risposta = risposta;
    }

    public boolean verificaRisposta(int risposta) {
        return this.risposta == risposta;
    }

    public int stringToInteger(String input) {
            return Integer.parseInt(input);
    }
}
