public class IndovinelloNumerico implements Indovinello{
    private String testo;
    private int risposta;
    private int id;

    public Indovinello(String testo, String risposta)
    {
        this.testo = testo;
        this.risposta = risposta;
    }

    public String getTesto()
    {
        return this.testo;
    }

    public String getRisposta()
    {
        return this.risposta;
    }

    public int getId() {
        return this.id;
    }

    public void setTesto(String testo){
        this.testo = testo;
    }

    public void setRisposta(int risposta) {
        this.risposta = risposta;
    }

    public boolean verificaRispostaRisposta(int risposta) {
        return this.risposta == risposta;
    }
}
