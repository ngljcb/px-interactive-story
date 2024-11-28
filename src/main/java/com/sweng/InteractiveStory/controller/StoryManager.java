package com.sweng.InteractiveStory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweng.InteractiveStory.model.StoryManagerModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/stories")
public class StoryManager {

    /**
     * Recupera tutte le storie dalla collezione "Storie".
     *
     * @return ResponseEntity contenente una lista di mappe, ciascuna con i dati delle storie.
     */
    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getStories() {
        try {
            // Crea un'istanza di StoryManagerModel
            StoryManagerModel storyManagerModel = new StoryManagerModel();

            // Recupera tutte le storie attraverso il model StoryManagerModel
            List<Map<String, String>> allStories = storyManagerModel.getAllStories();

            // Restituisce le storie come risposta JSON
            return ResponseEntity.ok(allStories);
        } catch (Exception e) {
            e.printStackTrace();
            // Restituisce un errore 500 in caso di eccezione
            return ResponseEntity.status(500).body(null);
        }
    }
}
