package com.sweng.InteractiveStory.entity.decisione;
import com.sweng.InteractiveStory.entity.Scenario;
import com.sweng.InteractiveStory.entity.utils.Oggetto;

public class Opzione {
    Oggetto oggetto;
    String nome;
    boolean sbloccato = false;

    public Opzione(Oggetto oggetto, String nome) {
        this.oggetto = oggetto;
        this.nome = nome;
        this.sbloccato = false;
    }
    
    Scenario goToScenario(Scenario scenarioAttuale, Scenario scenarioSuccessivo){
        if (sbloccato){
            return scenarioSuccessivo;
        } else {
            return scenarioAttuale;

        }
    }

    void verificaOggettoPosseduto(Oggetto oggetto){
        sbloccato = this.oggetto.getNome().equals(oggetto.getNome());
    }

    public String getNome() {
        return nome;
    }

}



