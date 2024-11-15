package com.sweng.InteractiveStory.entities.Utenti;
public class Visitatore implements Utente
{
    private String nome, password; // incapsulation

    public Visitatore(String nome, String password) {
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
}
//get listaStorie