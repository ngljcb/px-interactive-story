package com.sweng.InteractiveStory.adapter;

import com.google.cloud.firestore.DocumentSnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.Map;

public class SceltaIndovinelloAdapter {
    private final FirebaseDBManager dbManager;

    public SceltaIndovinelloAdapter() {
        this.dbManager = new FirebaseDBManager();
    }

    /**
     * Recupera i dati di un indovinello da Firebase.
     *
     * @param idStoria    L'ID della storia.
     * @param idScenario  L'ID dello scenario.
     * @return Una mappa contenente i dati dell'indovinello.
     * @throws Exception Se si verifica un errore durante il recupero dei dati.
     */
    public Map<String, String> getIndovinello(String idStoria, String idScenario) throws Exception {
        // Ottieni il documento dell'indovinello
        DocumentSnapshot indovinelloDoc = dbManager.getDocument(
                "Storie/" + idStoria + "/Scenari/" + idScenario + "/Indovinello",
                "indovinelloId" // Sostituisci con l'ID effettivo dell'indovinello
        );

        if (!indovinelloDoc.exists()) {
            throw new IllegalArgumentException("Indovinello non trovato per scenario: " + idScenario);
        }

        // Ritorna i dati come mappa
        Map<String, String> dati = new HashMap<>();
        dati.put("testo", (String) indovinelloDoc.get("testo"));
        dati.put("rispostaCorretta", (String) indovinelloDoc.get("rispostaCorretta"));
        dati.put("prox-scenario-corretto", (String) indovinelloDoc.get("prox-scenario-corretto"));
        dati.put("prox-scenario-errato", (String) indovinelloDoc.get("prox-scenario-errato"));
        return dati;
    }
}
