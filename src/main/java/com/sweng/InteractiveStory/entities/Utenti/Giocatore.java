package com.sweng.InteractiveStory.entities.Utenti;
public class Giocatore implements Utente {
    private String nome, password; // incapsulation

    public Giocatore(String nome, String password) {
        this.nome = nome;
        this.password = password;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // lista getStorieGiocate
    // set storiaGiocata
    // lista getDecisioniPrese
    // set decisionePresa
    // set rispostaIndovinello
    // lista indovinello risposti
}
