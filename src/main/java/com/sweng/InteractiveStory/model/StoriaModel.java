package com.sweng.InteractiveStory.adapter;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.sweng.InteractiveStory.service.FirebaseDBManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoriaAdapter {

    private final FirebaseDBManager dbManager;

    public StoriaAdapter() {
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
        dati.put("idscrittore", (String) storyDoc.get("idscrittore"));
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
}
