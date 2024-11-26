package com.sweng.InteractiveStory.adapter;

import com.google.cloud.firestore.DocumentSnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.Map;

public class SceltaOggettoAdapter {
    private final FirebaseDBManager dbManager;

    public SceltaOggettoAdapter() {
        this.dbManager = new FirebaseDBManager();
    }

    /**
     * Recupera i dati di un oggetto da Firebase.
     *
     * @param idStoria    L'ID della storia.
     * @param idScenario  L'ID dello scenario.
     * @return Una mappa contenente i dati dell'oggetto.
     * @throws Exception Se si verifica un errore durante il recupero dei dati.
     */
    public Map<String, String> getOggetto(String idStoria, String idScenario) throws Exception {
        // Ottieni il documento dell'oggetto
        DocumentSnapshot oggettoDoc = dbManager.getDocument(
                "Storie/" + idStoria + "/Scenari/" + idScenario + "/Oggetto",
                "oggettoId" // Sostituisci con l'ID effettivo dell'oggetto
        );

        if (!oggettoDoc.exists()) {
            throw new IllegalArgumentException("Oggetto non trovato per scenario: " + idScenario);
        }

        // Ritorna i dati come mappa
        Map<String, String> dati = new HashMap<>();
        dati.put("nome-oggetto", (String) oggettoDoc.get("nome-oggetto"));
        dati.put("prox-scenario-corretto", (String) oggettoDoc.get("prox-scenario-corretto"));
        dati.put("prox-scenario-errato", (String) oggettoDoc.get("prox-scenario-errato"));
        return dati;
    }
}
