package com.sweng.InteractiveStory.model;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    
    /**
     * Recupera tutte le storie dalla collezione "Storie".
     *
     * @return Una lista di mappe, ognuna contenente i dati di una storia.
     * @throws Exception Se si verificano errori durante il recupero.
     */
    public List<Map<String, String>> getScenariByStoriaId(String storiaId) throws Exception {
        // Percorso del documento specifico
        String collectionPath = "Storie/" + storiaId + "/Scenari/";
    
        // Ottieni i documenti dalla collezione
        QuerySnapshot sceneSnapshot = dbManager.getDocumentsFromCollection(collectionPath);
    
        // Helper per ottenere e convertire i valori in stringa
        Function<Object, String> safeToString = obj -> obj != null ? obj.toString() : "";
    
        // Recupera tutte le storie come lista di mappe da Firebase
        List<Map<String, String>> data = sceneSnapshot.getDocuments().stream().map(document -> {
            Map<String, String> scene = new HashMap<>();
            scene.put("id", document.getId());
            scene.put("descrizione", safeToString.apply(document.get("descrizione")));
            scene.put("oggetto", safeToString.apply(document.get("oggetto")));
            scene.put("order", safeToString.apply(document.get("order")));
            scene.put("tipo-scelta", safeToString.apply(document.get("tipo-scelta")));
            return scene;
        }).collect(Collectors.toList());
    
        return data;
    }
    
}
