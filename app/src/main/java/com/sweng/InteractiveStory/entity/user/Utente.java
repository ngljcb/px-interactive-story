package com.sweng.InteractiveStory.entity.user;

public interface Utente {
    String getNome();
    void setNome(String nome);
    void setPassword(String password);
    boolean verificaPassword(String password); // Controllo password
}
