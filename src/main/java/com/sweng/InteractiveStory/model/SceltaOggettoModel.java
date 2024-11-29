package com.sweng.InteractiveStory.model;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.Map;

public class SceltaOggettoModel {
    private final FirebaseDBManager dbManager;

    public SceltaOggettoModel() {
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

        // Percorso del documento specifico
        String documentPath = "Storie/" + idStoria + "/Scenari/" + idScenario + "/Oggetto";
    
        try {
            // Ottieni il QuerySnapshot della sottocollezione "Indovinello"
            QuerySnapshot oggettoDocSnapshot = dbManager.getDocumentsFromCollection(documentPath);
    
            // Assicurati che la collezione non sia vuota
            if (oggettoDocSnapshot.isEmpty()) {
                throw new IllegalArgumentException("SceltaOggetto non trovato per scenario: " + idScenario);
            }
    
            // Ottieni il primo documento della collezione (se applicabile)
            DocumentSnapshot oggettoDoc = oggettoDocSnapshot.getDocuments().get(0);
    
            // Verifica che tutti i campi richiesti siano presenti
            if (!oggettoDoc.contains("nome-oggetto") || 
                !oggettoDoc.contains("prox-scenario-corretto") || 
                !oggettoDoc.contains("prox-scenario-errato")) {
                throw new IllegalStateException("Il documento Indovinello non contiene tutti i campi richiesti.");
            }
    
            // Ritorna i dati come mappa
            Map<String, String> dati = new HashMap<>();
            dati.put("nome-oggetto", (String) oggettoDoc.get("nome-oggetto"));
            dati.put("prox-scenario-corretto", (String) oggettoDoc.get("prox-scenario-corretto"));
            dati.put("prox-scenario-errato", (String) oggettoDoc.get("prox-scenario-errato"));
            return dati;
    
        } catch (Exception e) {
            // Log dettagliato per il debug
            System.err.println("Errore durante il recupero della sceltaoggetto: " + e.getMessage());
            throw new Exception("Errore durante il recupero della sceltaoggetto: " + e.getMessage(), e);
        }
    }
}
