package com.sweng.InteractiveStory.entity.user;

public class Giocatore {
    private String uid;
    private String nome;
    private String email;

    // Costruttore
    public Giocatore(String uid, String nome, String email) {
        this.uid = uid;
        this.nome = nome;
        this.email = email;
    }

    public Giocatore() {
        this.uid = null;
        this.nome = null;
        this.email = null;
    }

    // Getter e Setter
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

