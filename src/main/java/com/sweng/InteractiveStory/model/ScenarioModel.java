package com.sweng.InteractiveStory.model;

import com.google.cloud.firestore.DocumentSnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.Map;

public class ScenarioModel {
    private final FirebaseDBManager dbManager;

    public ScenarioModel() {
        this.dbManager = new FirebaseDBManager();
    }

    /**
     * Recupera i dettagli di un singolo scenario.
     *
     * @param idScenario L'ID dello scenario.
     * @return Una mappa contenente i dettagli dello scenario.
     * @throws Exception Se si verificano errori durante il recupero.
     */
    public Map<String, String> getScenario(String idStoria, String idScenario) throws Exception {
        // Percorso del documento specifico
        String documentPath = "Storie/" + idStoria + "/Scenari/" + idScenario;
        
        // Ottieni il documento specifico dallo scenario
        DocumentSnapshot documentSnapshot = dbManager.getDocument(documentPath);
        
        if (!documentSnapshot.exists()) {
            throw new IllegalArgumentException("Scenario non trovato: " + idScenario);
        }
        
        // Crea una mappa dei dati dello scenario
        Map<String, String> dati = new HashMap<>();
        dati.put("id", idScenario);
        dati.put("descrizione", documentSnapshot.getString("descrizione"));
        dati.put("order", String.valueOf(documentSnapshot.get("order")));
        dati.put("tipoScelta", documentSnapshot.getString("tipo-scelta"));
        dati.put("oggetto", documentSnapshot.getString("oggetto"));

        return dati;
    }
}
