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
            storyData.put("idscrittore", (String) document.get("idscrittore"));
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
}
