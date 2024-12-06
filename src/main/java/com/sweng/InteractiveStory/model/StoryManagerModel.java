package com.sweng.InteractiveStory.model;

import com.google.cloud.firestore.QuerySnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoryManagerModel {

    private final FirebaseDBManager dbManager;

    public StoryManagerModel() {
        this.dbManager = new FirebaseDBManager();
    }

    /**
     * Recupera tutte le storie dalla collezione "Storie".
     *
     * @return Una lista di mappe, ognuna contenente i dati di una storia.
     * @throws Exception Se si verificano errori durante il recupero.
     */
    public List<Map<String, String>> getAllStories() throws Exception {
        QuerySnapshot storiesSnapshot = dbManager.getDocumentsFromCollection("Storie");

        // Recupera tutte le storie come lista di mappe da Firebase
        List<Map<String, String>> allStoriesData = storiesSnapshot.getDocuments().stream().map(document -> {
            Map<String, String> storyData = new HashMap<>();
            storyData.put("id", document.getId());
            storyData.put("titolo", (String) document.get("titolo"));
            storyData.put("descrizione", (String) document.get("descrizione"));
            storyData.put("idscrittore", (String) document.get("user-id"));
            return storyData;
        }).collect(Collectors.toList());

        return allStoriesData.stream()
                .map(storyData -> Map.of(
                        "id", storyData.get("id"),
                        "title", storyData.get("titolo"), // Rinomina "titolo" in "title"
                        "description", storyData.get("descrizione") // Rinomina "descrizione" in "description"
                ))
                .collect(Collectors.toList());
    }
    
    /**
     * Recupera tutte le storie dalla collezione "Storie".
     *
     * @return Una lista di mappe, ognuna contenente i dati di una storia.
     * @throws Exception Se si verificano errori durante il recupero.
     */
    public List<Map<String, String>> getAllStoriesByUser(String userid) throws Exception {
        QuerySnapshot storiesSnapshot = dbManager.getDocsByCondition("Storie", "user-id", userid);

        // Recupera tutte le storie come lista di mappe da Firebase
        List<Map<String, String>> allStoriesData = storiesSnapshot.getDocuments().stream().map(document -> {
            Map<String, String> storyData = new HashMap<>();
            storyData.put("id", document.getId());
            storyData.put("titolo", (String) document.get("titolo"));
            storyData.put("descrizione", (String) document.get("descrizione"));
            storyData.put("idscrittore", (String) document.get("user-id"));
            return storyData;
        }).collect(Collectors.toList());

        return allStoriesData.stream()
                .map(storyData -> Map.of(
                        "id", storyData.get("id"),
                        "title", storyData.get("titolo"),
                        "description", storyData.get("descrizione")))
                .collect(Collectors.toList());
    }

    /**
     * Elimina una storia dalla collezione "Storie".
     *
     * @param idStoria L'ID della storia da eliminare.
     * @throws Exception Se si verificano errori durante l'eliminazione.
     */
    public void deleteStory(String idStoria) throws Exception {
        dbManager.deleteDocument("Storie", idStoria);
    }

    /**
     * Aggiunge una storia con un ID generato automaticamente.
     *
     * @param storyData I dati della storia.
     * @return L'ID generato della nuova storia.
     * @throws Exception In caso di errore durante l'aggiunta.
     */
    public String addStoryWithGeneratedId(String collectionPath,Map<String, Object> storyData) throws Exception {
        try {
            return dbManager.addDocumentWithGeneratedId(collectionPath, storyData);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiunta della storia.", e);
        }
    }

    /**
     * Aggiunge uno scenario a una storia esistente.
     *
     * @param storyId      ID della storia.
     * @param scenarioId   ID dello scenario.
     * @param scenarioData I dati dello scenario.
     * @throws Exception In caso di errore durante l'aggiunta.
     */
    public void addScenario(String storyId, String scenarioId, Map<String, Object> scenarioData) throws Exception {
        try {
            String collectionPath = "Storie/" + storyId + "/Scenari";
            dbManager.addDocument(collectionPath, scenarioId, scenarioData);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiunta dello scenario alla storia: " + storyId, e);
        }
    }

    /**
     * Aggiunge uno scenario a una storia esistente.
     *
     * @param storyId      ID della storia.
     * @param scenarioId   ID dello scenario.
     * @param scenarioData I dati dello scenario.
     * @throws Exception In caso di errore durante l'aggiunta.
     */
    public void addIndovinello(String storyId, String scenarioId, Map<String, Object> scenarioData) throws Exception {
        try {
            String collectionPath = "Storie/" + storyId + "/Scenari/" + scenarioId + "/Indovinello";
            dbManager.addDocument(collectionPath, scenarioId, scenarioData);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiunta dello scenario alla storia: " + storyId, e);
        }
    }

    /**
     * Aggiunge uno scenario a una storia esistente.
     *
     * @param storyId      ID della storia.
     * @param scenarioId   ID dello scenario.
     * @param scenarioData I dati dello scenario.
     * @throws Exception In caso di errore durante l'aggiunta.
     */
    public void addOggetto(String storyId, String scenarioId, Map<String, Object> scenarioData) throws Exception {
        try {
            String collectionPath = "Storie/" + storyId + "/Scenari/" + scenarioId + "/Oggetto";
            dbManager.addDocument(collectionPath, scenarioId, scenarioData);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiunta dello scenario alla storia: " + storyId, e);
        }
    }


    /**
     * Modifica una storia esistente.
     *
     * @param storyId   ID della storia da modificare.
     * @param storyData I nuovi dati della storia.
     * @throws Exception In caso di errore durante la modifica.
     */
    public void updateStory(String storyId, Map<String, Object> storyData) throws Exception {
        try {
            String collectionPath = "Storie";
            dbManager.modifyDocument(collectionPath, storyId, storyData);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiornamento della storia: " + storyId, e);
        }
    }

    /**
     * Modifica uno scenario di una storia esistente.
     *
     * @param storyId      ID della storia.
     * @param scenarioId   ID dello scenario da modificare.
     * @param scenarioData I nuovi dati dello scenario.
     * @throws Exception In caso di errore durante la modifica.
     */
    public void updateScenario(String storyId, String scenarioId, Map<String, Object> scenarioData) throws Exception {
        try {
            String subCollectionPath = "Storie/" + storyId + "/Scenari";
            dbManager.modifyDocument(subCollectionPath, scenarioId, scenarioData);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiornamento dello scenario: " + scenarioId, e);
        }
    }

    /**
     * Modifica un indovinello esistente in uno scenario.
     *
     * @param storyId       ID della storia.
     * @param scenarioId    ID dello scenario.
     * @param indovinelloData I nuovi dati dell'indovinello.
     * @throws Exception In caso di errore durante la modifica.
     */
    public void updateIndovinello(String storyId, String scenarioId, Map<String, Object> indovinelloData) throws Exception {
        try {
            String indovinelloPath = "Storie/" + storyId + "/Scenari/" + scenarioId + "/Indovinello";
            dbManager.modifyCollection(indovinelloPath, scenarioId, indovinelloData);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiornamento dell'indovinello per lo scenario: " + scenarioId, e);
        }
    }

    /**
     * Modifica un oggetto esistente in uno scenario.
     *
     * @param storyId       ID della storia.
     * @param scenarioId    ID dello scenario.
     * @param oggettoData   I nuovi dati dell'oggetto.
     * @throws Exception In caso di errore durante la modifica.
     */
    public void updateOggetto(String storyId, String scenarioId, Map<String, Object> oggettoData) throws Exception {
        try {
            String oggettoPath = "Storie/" + storyId + "/Scenari/" + scenarioId + "/Oggetto";
            dbManager.modifyCollection(oggettoPath, scenarioId, oggettoData);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiornamento dell'oggetto per lo scenario: " + scenarioId, e);
        }
    }
}