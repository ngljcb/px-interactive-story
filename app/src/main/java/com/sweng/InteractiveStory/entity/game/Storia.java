package com.sweng.InteractiveStory.entity.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sweng.InteractiveStory.model.ScenarioModel;
import com.sweng.InteractiveStory.model.StoriaModel;

public class Storia {
    private String id;
    private String titolo;
    private String descrizione;
    private String idscrittore;
    private List<Scenario> scenari;

    private StoriaModel storiaModel;
    private ScenarioModel scenarioModel;

    public Storia() {
        this.titolo = "";
        this.descrizione = "";
        this.scenari = new ArrayList<>();

        storiaModel = new StoriaModel();
        scenarioModel = new ScenarioModel();
    }

    public Storia(String id, String titolo, String descrizione, String idscrittore) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.idscrittore = idscrittore;
        this.scenari = new ArrayList<>();

        storiaModel = new StoriaModel();
        scenarioModel = new ScenarioModel();
    }
    
    /**
     * Recupera la storia e popola le proprietà della classe.
     *
     * @param idStoria L'ID della storia da caricare.
     * @param storiaAdapter L'istanza di StoriaAdapter per ottenere i dettagli della storia.
     * @throws Exception Se si verifica un errore durante il caricamento dei dati.
     */
    public void getStoria(String idStoria) throws Exception {

        // L'adapter fornisce i dati come mappa
        Map<String, String> dati = storiaModel.getStoria(idStoria);

        this.id = dati.get("id");
        this.titolo = dati.get("titolo");
        this.descrizione = dati.get("descrizione");
        this.idscrittore = dati.get("idscrittore");
    }

    /**
     * Recupera gli scenari della storia e popola la lista di scenari.
     *
     * @param idStoria L'ID della storia.
     * @param storiaModel L'istanza di StoriaAdapter per ottenere gli ID degli scenari.
     * @param scenarioModel L'istanza di ScenarioAdapter per ottenere i dettagli degli scenari.
     * @throws Exception Se si verificano errori durante il caricamento.
     */
    public void setScenari(String idStoria) throws Exception {
        // Ottieni l'elenco degli ID degli scenari associati alla storia
        String[] idScenari = storiaModel.getScenari(idStoria);

        // Per ogni ID dello scenario, ottieni i dettagli e crea un'istanza di Scenario
        for (String idScenario : idScenari) {
            Map<String, String> datiScenario = scenarioModel.getScenario(idStoria, idScenario);

            String id = datiScenario.get("id");
            String descrizione = datiScenario.get("descrizione");
            int order = Integer.parseInt(datiScenario.get("order"));
            String tipoScelta = datiScenario.get("tipoScelta");
            String oggetto = datiScenario.get("oggetto");

            // Crea l'istanza dello scenario
            Scenario scenario = new Scenario(idStoria, id, descrizione, order, tipoScelta, oggetto);

            // Aggiungi lo scenario alla lista
            this.scenari.add(scenario);
        }
    }

    // Getter per ottenere la lista di scenari
    public List<Scenario> getScenariList() {
        return scenari;
    }
    
    public Scenario getPrimoScenario() {
        return scenari.stream()
                .filter(scenario -> scenario.getOrderNumber() == 1)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Primo scenario non trovato."));
    }

    // Metodo per aggiungere gli scenari
    public void addScenario(Scenario scenario) {
        this.scenari.add(scenario);
    }

    // Metodo per recuperare uno scenario dato il suo ID
    public Scenario getScenarioById(String idScenario) {
        return this.scenari.stream()
                .filter(scenario -> scenario.getId().equals(idScenario))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Scenario con ID " + idScenario + " non trovato."));
    }

    public String getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setIdScrittore(String idscrittore) {
        this.idscrittore = idscrittore;
    }

    public String getIdScrittore() {
        return idscrittore;
    }
}
