public class IndovinelloTestuale implements Indovinello{
    private String testo;
    private String risposta;
    private int id;

    public Indovinello(String testo, String risposta)
    {
        this.testo = testo;
        this.risposta = risposta; // dev'essere in minuscolo
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

    public void setRisposta(String risposta) {
        this.risposta = risposta;
    }

    // converte i testi in minuscolo
    private String convertiMinuscolo(String risposta){
        return risposta.toLowerCase();
    }

    // verifica la risposta
    public boolean verificaRispostaRisposta(String risposta) {
        return risposta.equals(this.risposta);
    }
}
