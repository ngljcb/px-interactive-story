package com.sweng.InteractiveStory.model;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.Map;

public class SceltaIndovinelloModel {
    private final FirebaseDBManager dbManager;

    public SceltaIndovinelloModel() {
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

        // Percorso del documento specifico
        String documentPath = "Storie/" + idStoria + "/Scenari/" + idScenario + "/Indovinello";
    
        try {
            // Ottieni il QuerySnapshot della sottocollezione "Indovinello"
            QuerySnapshot indovinelloSnapshot = dbManager.getDocumentsFromCollection(documentPath);
    
            // Assicurati che la collezione non sia vuota
            if (indovinelloSnapshot.isEmpty()) {
                throw new IllegalArgumentException("Indovinello non trovato per scenario: " + idScenario);
            }
    
            // Ottieni il primo documento della collezione (se applicabile)
            DocumentSnapshot indovinelloDoc = indovinelloSnapshot.getDocuments().get(0);
    
            // Verifica che tutti i campi richiesti siano presenti
            if (!indovinelloDoc.contains("testo") || 
                !indovinelloDoc.contains("risposta") || 
                !indovinelloDoc.contains("prox-scenario-corretto") || 
                !indovinelloDoc.contains("prox-scenario-errato")) {
                throw new IllegalStateException("Il documento Indovinello non contiene tutti i campi richiesti.");
            }
    
            // Ritorna i dati come mappa
            Map<String, String> dati = new HashMap<>();
            dati.put("testo", indovinelloDoc.getString("testo"));
            dati.put("risposta", indovinelloDoc.getString("risposta"));
            dati.put("prox-scenario-corretto", indovinelloDoc.getString("prox-scenario-corretto"));
            dati.put("prox-scenario-errato", indovinelloDoc.getString("prox-scenario-errato"));
            return dati;
    
        } catch (Exception e) {
            // Log dettagliato per il debug
            System.err.println("Errore durante il recupero dell'indovinello: " + e.getMessage());
            throw new Exception("Errore durante il recupero dell'indovinello: " + e.getMessage(), e);
        }
    }
    
}
