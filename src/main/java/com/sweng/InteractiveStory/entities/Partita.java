package com.sweng.InteractiveStory.entities;

import java.text.SimpleDateFormat;
import java.util.*;

import com.sweng.InteractiveStory.entities.Utenti.Giocatore;
import com.sweng.InteractiveStory.entities.indovinelli.IndovinelloNumerico;
import com.sweng.InteractiveStory.entities.Utenti.Utente;
import com.sweng.InteractiveStory.entities.Scenario;
import com.sweng.InteractiveStory.entities.decisione.Preferenza;
import com.sweng.InteractiveStory.entities.decisione.Scelta;
import com.sweng.InteractiveStory.entities.indovinelli.IndovinelloTestuale;
import com.sweng.InteractiveStory.entities.utils.Inventario;

public class Partita {
    Boolean stato;
    String dataInizio;
    String dataFine;
    Utente giocatore;
    Storia storia;
    Inventario oggetti;

    public Partita(Utente giocatore, Storia storia) {
        this.stato = false;
        this.dataInizio = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        this.dataFine = null;
        this.giocatore = giocatore;
        this.storia = storia;
        oggetti = new Inventario();
    }

    public Scenario attraversamentoScenario(Scenario scenarioAttuale, Scenario scenarioSuccessivo) {
        System.out.println(scenarioAttuale.getDescrizione());
        mostraScelta(scenarioAttuale);
        boolean b = attendiRisposta(scenarioAttuale);
        return esito(scenarioAttuale, scenarioSuccessivo, b);
    }

    public void mostraScelta(Scenario scenarioAttuale) {
        if (scenarioAttuale.scelta instanceof Preferenza){
            Preferenza preferenza = (Preferenza) scenarioAttuale.scelta;
            System.out.println(preferenza.getDescrizione());
            System.out.println("A: " + preferenza.getOpzione1().getNome());
            System.out.println("B: " + preferenza.getOpzione2().getNome());
        } else if (scenarioAttuale.scelta instanceof IndovinelloNumerico){
            IndovinelloNumerico indovinello = (IndovinelloNumerico) scenarioAttuale.scelta;
            System.out.println(indovinello.getTesto());

        } else {
            IndovinelloTestuale indovinello = (IndovinelloTestuale) scenarioAttuale.scelta;
            System.out.println(indovinello.getTesto());
        }
    }

    public boolean attendiRisposta(Scenario scenarioAttuale) {
        Scanner scanner = new Scanner(System.in);
        String risposta = scanner.nextLine();
        scanner.close();
        boolean esito = false;
        if (scenarioAttuale.scelta instanceof IndovinelloNumerico){
            IndovinelloNumerico indovinello = (IndovinelloNumerico) scenarioAttuale.scelta;
            esito = indovinello.verificaRisposta(Integer.parseInt(risposta));

        } else {
            IndovinelloTestuale indovinello = (IndovinelloTestuale) scenarioAttuale.scelta;
            esito = indovinello.verificaRisposta(risposta);
        }
        return esito;
    }

    public Scenario esito(Scenario scenarioAttuale, Scenario scenarioSuccessivo, boolean esito) {
        if (esito){
            Scelta scelta = scenarioAttuale.scelta;
            return scelta.goToScenario(scenarioAttuale, scenarioSuccessivo);//scenariosuccessivo(?)
        } else {
           return scenarioAttuale;
        }
    }
    /**
     * attraversamento:
     *  per ogni scenario:
     *      mostra desc
     *      mostra scelta (decisione / indovinello)
     *      attende risposta
     *      esito ( scenario attuale, A, B) 
     */ 

    public Boolean getStato() {
        return stato;
    }

    public void setStato(Boolean stato) {
        this.stato = stato;
    }

    public String getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getDataFine() {
        return dataFine;
    }

    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    public Utente getGiocatore() {
        return giocatore;
    }

    public void setGiocatore(Giocatore giocatore) {
        this.giocatore = giocatore;
    }

    public Storia getStoria() {
        return storia;
    }

    public void setStoria(Storia storia) {
        this.storia = storia;
    }
    


}
