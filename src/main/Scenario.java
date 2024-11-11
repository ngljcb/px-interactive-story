

public class Scenario {
    String descrizione;
    String tipologia;

    public Scenario(String descrizione, String tipologia) {
        this.descrizione = descrizione;
        this.tipologia = tipologia;
    }

public String getDescrizione() {
    return descrizione;
}

public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
}

public String getTipologia() {
    return tipologia;
}

public void setTipologia(String tipologia) {
    this.tipologia = tipologia;
}
}
