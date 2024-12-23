package com.sweng.InteractiveStory.model;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoriaModel {

    private final FirebaseDBManager dbManager;

    public StoriaModel() {
        this.dbManager = new FirebaseDBManager();
    }

    /**
     * Recupera i dati di una storia e li restituisce come una mappa.
     *
     * @param idStoria L'ID della storia da recuperare.
     * @return Una mappa contenente i dati della storia.
     * @throws Exception Se si verificano errori durante il recupero dei dati.
     */
    public Map<String, String> getStoria(String idStoria) throws Exception {
        DocumentSnapshot storyDoc = dbManager.getDocument("Storie", idStoria);
        if (!storyDoc.exists()) {
            throw new IllegalArgumentException("Storia non trovata: " + idStoria);
        }

        // Ritorna i dati come mappa
        Map<String, String> dati = new HashMap<>();
        dati.put("id", idStoria);
        dati.put("titolo", (String) storyDoc.get("titolo"));
        dati.put("descrizione", (String) storyDoc.get("descrizione"));
        dati.put("idscrittore", (String) storyDoc.get("user-id"));
        return dati;
    }

    /**
     * Ottiene un elenco di ID degli scenari associati a una storia.
     *
     * @param idStoria L'ID della storia.
     * @return Un array di stringhe contenente gli ID degli scenari.
     * @throws Exception Se si verificano errori durante il recupero.
     */
    public String[] getScenari(String idStoria) throws Exception {
        // Ottieni il QuerySnapshot della sottocollezione "Scenari"
        QuerySnapshot scenariSnapshot = dbManager.getDocumentsFromCollection("Storie/" + idStoria + "/Scenari");

        // Mappa gli ID degli scenari in una lista
        List<String> idScenari = scenariSnapshot.getDocuments().stream()
                .map(DocumentSnapshot::getId)
                .collect(Collectors.toList());

        // Converti la lista in un array di stringhe
        return idScenari.toArray(new String[0]);
    }

    /**
     * Salva una storia e uno scenario nella collezione "SavedStory" sotto un utente, includendo gli oggetti.
     *
     * @param userId       L'ID dell'utente.
     * @param storyId      L'ID della storia da salvare.
     * @param scenarioId   L'ID dello scenario da salvare.
     * @param inventory    Array di stringhe rappresentanti gli oggetti nell'inventario.
     * @throws Exception   In caso di errore durante il salvataggio.
     */
    public void saveStory(String userId, String storyId, String scenarioId, String[] inventory) throws Exception {
        try {
            String collectionPath = "Utente/" + userId + "/SavedStory";
            Map<String, Object> savedStoryData = new HashMap<>();
            savedStoryData.put("storiaId", storyId);
            savedStoryData.put("scenarioId", scenarioId);
            if(inventory != null && inventory.length != 0) savedStoryData.put("inventory", inventory);

            // Genera un ID unico per il documento salvato
            dbManager.addDocumentWithGeneratedId(collectionPath, savedStoryData);
        } catch (Exception e) {
            throw new Exception("Errore durante il salvataggio della storia per l'utente: " + userId, e);
        }
    }

    /**
     * Recupera una storia salvata da un utente nella collezione "SavedStory", filtrata per `storiaId`.
     *
     * @param userId   L'ID dell'utente.
     * @param storiaId L'ID della storia da filtrare.
     * @return Una mappa contenente i dettagli della storia salvata, o null se non trovata.
     * @throws Exception In caso di errore durante il recupero.
     */
    public Map<String, Object> getSavedStory(String userId, String storiaId) throws Exception {
        try {
            String collectionPath = "Utente/" + userId + "/SavedStory";

            // Recupera i documenti filtrati per storiaId
            QuerySnapshot querySnapshot = dbManager.getDocsByCondition(collectionPath, "storiaId", storiaId);

            if (querySnapshot.isEmpty()) {
                return null; // Ritorna null se non esiste alcun documento con il filtro specificato
            }

            // Recupera il primo documento trovato (assumendo che sia univoco)
            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
            Map<String, Object> savedStoryData = new HashMap<>();
            savedStoryData.put("storiaId", document.getString("storiaId"));
            savedStoryData.put("scenarioId", document.getString("scenarioId"));
            savedStoryData.put("inventory", document.get("inventory")); // Ritorna l'array di oggetti
            return savedStoryData;

        } catch (Exception e) {
            throw new Exception("Errore durante il recupero della storia salvata per l'utente: " + userId + " e storiaId: " + storiaId, e);
        }
    }

    /**
     * Elimina una storia salvata specifica dalla collezione "SavedStory" di un utente.
     *
     * @param userId    L'ID dell'utente.
     * @param storyId   L'ID della storia da eliminare.
     * @throws Exception In caso di errore durante l'eliminazione.
     */
    public void deleteSavedStory(String userId, String storyId) throws Exception {
        try {
            String collectionPath = "Utente/" + userId + "/SavedStory";
            QuerySnapshot querySnapshot = dbManager.getDocsByCondition(collectionPath, "storiaId", storyId);

            if (!querySnapshot.isEmpty()) {
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    dbManager.deleteDocument(collectionPath, document.getId());
                }
            } else {
                throw new Exception("Nessuna storia trovata con l'ID: " + storyId);
            }
        } catch (Exception e) {
            throw new Exception("Errore durante l'eliminazione della storia salvata per l'utente: " + userId, e);
        }
    }
}
