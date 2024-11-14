import java.text.SimpleDateFormat;
import java.util.*;

public class Partita {
    Boolean stato;
    String dataInizio;
    String dataFine;
    Utente giocatore;
    Storia storia;

    public Partita(Utente giocatore, Storia storia) {
        this.stato = false;
        this.dataInizio = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.dataFine = null;
        this.giocatore = giocatore;
        this.storia = storia;
    }
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
    

    
    //set/get giocatore
    //set/get scenario
    //set/get indovinello

}
