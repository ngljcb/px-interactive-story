package com.sweng.InteractiveStory.entity.game;

import java.util.Map;

import com.sweng.InteractiveStory.adapter.SceltaIndovinelloAdapter;
import com.sweng.InteractiveStory.adapter.SceltaOggettoAdapter;
import com.sweng.InteractiveStory.entity.option.Indovinello;
import com.sweng.InteractiveStory.entity.option.IndovinelloTestuale;
import com.sweng.InteractiveStory.entity.option.Scelta;
import com.sweng.InteractiveStory.entity.option.SceltaIndovinello;
import com.sweng.InteractiveStory.entity.option.SceltaOggetto;

public class Scenario {
    private String id;
    private String descrizione;
    private int order;
    private String tipoScelta;
    private String oggetto;
    private String idStoria;
    private Scelta scelte;

    public Scenario(String idStoria, String idScenario, String descrizione, int order, String tipoScelta,
            String oggetto) {
        this.id = idScenario;
        this.descrizione = descrizione;
        this.order = order;
        this.tipoScelta = tipoScelta;
        this.oggetto = oggetto;
        this.idStoria = idStoria;
    }
    
    /**
     * Recupera l'indovinello da Firebase tramite l'IndovinelloAdapter e lo associa come SceltaIndovinello.
     *
     * @param idScenario L'ID dello scenario.
     * @param adapter    L'adapter che gestisce l'accesso ai dati degli indovinelli.
     * @throws Exception Se si verifica un errore durante il recupero dei dati.
     */
    public void getIndovinello(String idScenario, SceltaIndovinelloAdapter adapter) throws Exception {
        // Chiama l'adapter per ottenere i dati dell'indovinello
        Map<String, String> indovinelloData = adapter.getIndovinello(this.idStoria, idScenario);

        // Crea un'istanza di Indovinello
        Indovinello indovinello = new IndovinelloTestuale(
                indovinelloData.get("testo"),
                indovinelloData.get("rispostaCorretta"));

        // Ottieni gli ID degli scenari corretti ed errati
        String idScenarioCorretto = indovinelloData.get("prox-scenario-corretto");
        String idScenarioErrato = indovinelloData.get("prox-scenario-errato");

        // Associa la SceltaIndovinello all'attributo scelte
        this.scelte = new SceltaIndovinello(indovinello, idScenarioCorretto, idScenarioErrato);
    }
    
    /**
     * Recupera l'oggetto da Firebase tramite il SceltaOggettoAdapter e lo associa come SceltaOggetto.
     *
     * @param idScenario L'ID dello scenario.
     * @param adapter    L'adapter che gestisce l'accesso ai dati degli oggetti.
     * @throws Exception Se si verifica un errore durante il recupero dei dati.
     */
    public void getOggetto(String idScenario, SceltaOggettoAdapter adapter) throws Exception {
        // Chiama l'adapter per ottenere i dati dell'oggetto
        Map<String, String> oggettoData = adapter.getOggetto(this.idStoria, idScenario);

        // Ottieni il nome dell'oggetto e gli ID degli scenari collegati
        String nomeOggetto = oggettoData.get("nome-oggetto");
        String idScenarioConOggetto = oggettoData.get("prox-scenario-corretto");
        String idScenarioSenzaOggetto = oggettoData.get("prox-scenario-errato");

        // Associa la SceltaOggetto all'attributo scelte
        this.scelte = new SceltaOggetto(nomeOggetto, idScenarioConOggetto, idScenarioSenzaOggetto);
    }

    // Getters e Setters...
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getOrderNumber() {
        return order;
    }

    public String getId() {
        return id;
    }

    public boolean hasScelte() {
        return scelte != null;
    }

    public Scelta getScelte() {
        return scelte;
    }

	public String getTipoScelta() {
		return tipoScelta;
	}
}
