package com.sweng.InteractiveStory.entity.decisione;
public class Preferenza {
    private String descrizione;
    private Opzione opzione1;
    private Opzione opzione2;

    public Preferenza(String descrizione, Opzione opzione1, Opzione opzione2) {
        this.descrizione = descrizione;
        this.opzione1 = opzione1;
        this.opzione2 = opzione2;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Opzione getOpzione1() {
        return opzione1;
    }

    public void setOpzione1(Opzione opzione1) {
        this.opzione1 = opzione1;
    }

    public Opzione getOpzione2() {
        return opzione2;
    }

    public void setOpzione2(Opzione opzione2) {
        this.opzione2 = opzione2;
    }
}