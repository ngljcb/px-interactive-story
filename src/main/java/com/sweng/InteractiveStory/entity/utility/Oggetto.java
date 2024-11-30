package com.sweng.InteractiveStory.entity.utility;

import java.util.UUID;

public class Oggetto {
    private String nome;
    private String id;

    public Oggetto(String nome) {
        this.nome = nome;
        this.id = generateRandomId();
    }

    // Genera un ID casuale
    private String generateRandomId() {
        return UUID.randomUUID().toString();
    }

    public String getNome() {
        return this.nome;
    }

    public String getId() {
        return this.id;
    }
}

