package com.sweng.InteractiveStory.adapter;

import com.google.cloud.firestore.DocumentSnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.Map;

public class ScenarioAdapter {
    private final FirebaseDBManager dbManager;

    public ScenarioAdapter() {
        this.dbManager = new FirebaseDBManager();
    }

    /**
     * Recupera i dettagli di un singolo scenario.
     *
     * @param idScenario L'ID dello scenario.
     * @return Una mappa contenente i dettagli dello scenario.
     * @throws Exception Se si verificano errori durante il recupero.
     */
    public Map<String, String> getScenario(String idScenario) throws Exception {
        DocumentSnapshot scenarioDoc = dbManager.getDocument("Scenari", idScenario);
        if (!scenarioDoc.exists()) {
            throw new IllegalArgumentException("Scenario non trovato: " + idScenario);
        }

        Map<String, String> dati = new HashMap<>();
        dati.put("id", idScenario);
        dati.put("descrizione", (String) scenarioDoc.get("descrizione"));
        dati.put("order", String.valueOf(scenarioDoc.get("order")));
        dati.put("tipoScelta", (String) scenarioDoc.get("tipo-scelta"));
        dati.put("oggetto", (String) scenarioDoc.get("oggetto"));
        return dati;
    }
}
