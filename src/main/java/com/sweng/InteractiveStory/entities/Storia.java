import java.util.ArrayList;

public class Storia {
    String titolo;
    String descrizione;
    ArrayList<Scenario> scenari;

    public Storia(String titolo, String descrizione, ArrayList<Scenario> scenari) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.scenari = scenari;
    }

public String getTitolo() {
    return this.titolo;
}

public void setTitolo(String titolo) {
    this.titolo = titolo;
}

public String getDescrizione() {
    return descrizione;
}

public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
}

public ArrayList<Scenario> getScenari() {
    return scenari;
}

public void setScenari(ArrayList<Scenario> scenari) {
    this.scenari = scenari;
}
}