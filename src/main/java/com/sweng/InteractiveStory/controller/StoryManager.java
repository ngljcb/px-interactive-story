package com.sweng.InteractiveStory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweng.InteractiveStory.model.StoryManagerModel;
import com.sweng.InteractiveStory.model.SceltaIndovinelloModel;
import com.sweng.InteractiveStory.model.SceltaOggettoModel;
import com.sweng.InteractiveStory.model.ScenarioModel;
import com.sweng.InteractiveStory.model.StoriaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/stories")
public class StoryManager {

    private static final Logger logger = LoggerFactory.getLogger(StoryManager.class);

    /**
     * Recupera tutte le storie dalla collezione "Storie".
     *
     * @return ResponseEntity contenente una lista di mappe, ciascuna con i dati delle storie.
     */
    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getStories() {
        try {
            StoryManagerModel storyManagerModel = new StoryManagerModel();
            List<Map<String, String>> allStories = storyManagerModel.getAllStories();
            return ResponseEntity.ok(allStories);
        } catch (Exception e) {
            logger.error("Errore durante il recupero delle storie.", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Recupera tutte le storie dalla collezione "Storie".
     *
     * @return ResponseEntity contenente una lista di mappe, ciascuna con i dati delle storie.
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Map<String, String>>> getStoriesByUser(@PathVariable("id") String userid) {
        try {
            StoryManagerModel storyManagerModel = new StoryManagerModel();
            List<Map<String, String>> allStories = storyManagerModel.getAllStoriesByUser(userid);
            return ResponseEntity.ok(allStories);
        } catch (Exception e) {
            logger.error("Errore durante il recupero delle storie.", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Recupera una storia con i relativi scenari e dettagli.
     *
     * @param storyId L'ID della storia da recuperare.
     * @return ResponseEntity contenente i dettagli della storia e scenari nel formato JSON.
     */
    @GetMapping("/{id}/modify")
    public ResponseEntity<Map<String, Object>> getStoryById(@PathVariable("id") String storyId) {
        try {
            // Istanziazione dei model
            StoriaModel storyModel = new StoriaModel();
            ScenarioModel scenarioModel = new ScenarioModel();
            SceltaIndovinelloModel indovinelloModel = new SceltaIndovinelloModel();
            SceltaOggettoModel oggettoModel = new SceltaOggettoModel();

            // Recupero i dati della storia
            Map<String, String> story = storyModel.getStoria(storyId);
            if (story == null || story.isEmpty()) {
                logger.warn("Nessuna storia trovata con ID: {}", storyId);
                return ResponseEntity.status(404).body(null);
            }

            // Recupero gli scenari della storia
            List<Map<String, String>> rawScenarios = scenarioModel.getScenariByStoriaId(storyId);

            // Conversione da List<Map<String, String>> a List<Map<String, Object>>
            List<Map<String, Object>> scenari = new ArrayList<>();
            for (Map<String, String> rawScenario : rawScenarios) {
                scenari.add(new HashMap<>(rawScenario));
            }

            // Iterazione sugli scenari per aggiungere i dettagli specifici
            for (Map<String, Object> scenario : scenari) {
                String scenarioId = String.valueOf(scenario.getOrDefault("id", ""));
                String tipoScelta = String.valueOf(scenario.getOrDefault("tipo-scelta", ""));

                if ("indovinello".equalsIgnoreCase(tipoScelta)) {
                    // Recupero i dettagli dell'indovinello
                    Map<String, String> indovinelloData = indovinelloModel.getIndovinello(storyId, scenarioId);
                    if (indovinelloData != null) {
                        scenario.put("Indovinello", indovinelloData);
                    }
                } else if ("oggetto".equalsIgnoreCase(tipoScelta)) {
                    // Recupero i dettagli dell'oggetto
                    Map<String, String> oggettoData = oggettoModel.getOggetto(storyId, scenarioId);
                    if (oggettoData != null) {
                        scenario.put("Oggetto", oggettoData);
                    }
                }
            }

            // Creazione del JSON di risposta
            Map<String, Object> response = new HashMap<>();
            response.put("idscrittore", story.get("idscrittore"));
            response.put("titolo", story.get("titolo"));
            response.put("descrizione", story.get("descrizione"));
            response.put("Scenari", scenari);

            logger.debug("Risposta JSON generata: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Errore durante il recupero della storia e degli scenari.", e);
            return ResponseEntity.status(500).body(null);
        }
    }


    /**
     * Elimina una storia dalla collezione "Storie".
     *
     * @param idStoria L'ID della storia da eliminare.
     * @return ResponseEntity che indica il risultato dell'operazione.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStory(@PathVariable("id") String idStoria) {
        logger.debug("Richiesta per eliminare la storia con ID: {}", idStoria);
        try {
            // Crea un'istanza di StoryManagerModel
            StoryManagerModel storyManagerModel = new StoryManagerModel();
            storyManagerModel.deleteStory(idStoria);

            logger.debug("Storia con ID {} eliminata con successo.", idStoria);

            // Restituisce un messaggio di successo
            return ResponseEntity.ok("Storia eliminata con successo.");
        } catch (Exception e) {
            logger.error("Errore durante l'eliminazione della storia con ID: {}", idStoria, e);
            // Restituisce un errore 500 in caso di eccezione
            return ResponseEntity.status(500).body("Errore durante l'eliminazione della storia.");
        }
    }

    @PostMapping
    public ResponseEntity<String> addStory(@RequestBody Map<String, Object> storyData) {
        logger.debug("Richiesta per aggiungere una nuova storia.");
        try {
            // Crea un'istanza di CollectionAdapter
            StoryManagerModel storyManagerModel = new StoryManagerModel();

            // Validazione e creazione della storia
            String idStoria = createStory(storyData, storyManagerModel);
            logger.debug("Storia creata con successo. ID generato: {}", idStoria);

            // Aggiunta degli scenari
            logger.debug("Inizio aggiunta scenari per la storia con ID: {}", idStoria);
            addScenariosToStory(storyData, idStoria, storyManagerModel);
            logger.debug("Tutti gli scenari sono stati aggiunti correttamente per la storia con ID: {}", idStoria);

            return ResponseEntity.ok("Storia e scenari aggiunti con successo.");
        } catch (IllegalArgumentException e) {
            logger.warn("Errore di validazione: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Errore durante l'aggiunta della storia e degli scenari", e);
            return ResponseEntity.status(500).body("Errore durante l'aggiunta della storia e degli scenari.");
        }
    }

    private String createStory(Map<String, Object> storyData, StoryManagerModel storyManagerModel) throws Exception {
        logger.debug("Creazione di una nuova storia con i dati: {}", storyData);

        String titolo = extractString(storyData, "titolo");
        String descrizione = extractString(storyData, "descrizione");
        String userid = extractString(storyData, "user-id");

        logger.debug("Titolo: '{}', Descrizione: '{}', User ID: '{}'", titolo, descrizione, userid);

        Map<String, Object> storyMap = Map.of(
            "titolo", titolo,
            "descrizione", descrizione,
            "user-id", userid
        );

        String idStoria = storyManagerModel.addStoryWithGeneratedId("Storie", storyMap);
        logger.debug("Storia aggiunta con ID: {}", idStoria);

        return idStoria;
    }

    private void addScenariosToStory(Map<String, Object> storyData, String idStoria, StoryManagerModel storyManagerModel) throws Exception {
        Logger logger = LoggerFactory.getLogger(StoryManager.class);

        Object rawScenarios = storyData.get("scenari");
        
        List<?> scenariosList = (List<?>) rawScenarios;
        logger.debug("Numero di scenari ricevuti: {}", scenariosList.size());

        for (int i = 0; i < scenariosList.size(); i++) {
            Object scenarioObj = scenariosList.get(i);

            Map<String, Object> scenarioData = safeCastToMap(scenarioObj, "Scenario in formato non valido.");
            logger.debug("Elaborazione dello scenario {}: {}", i + 1, scenarioData);

            createScenario(idStoria, scenarioData, storyManagerModel);
        }
    }

    private void createScenario(String idStoria, Map<String, Object> scenarioData, StoryManagerModel storyManagerModel)
            throws Exception {
        Logger logger = LoggerFactory.getLogger(StoryManager.class);

        String idScenario = extractString(scenarioData, "order");
        int order = extractInteger(scenarioData, "order");
        String descrizioneScenario = extractString(scenarioData, "description");
        String tipoScelta = extractString(scenarioData, "choiceType");
        String oggetto = extractString(scenarioData, "foundObject");

        logger.debug("Creazione scenario. ID: {}, Ordine: {}, Descrizione: '{}', Tipo Scelta: '{}', Oggetto: '{}'",
                idScenario, order, descrizioneScenario, tipoScelta, oggetto);

        Map<String, Object> scenarioMap = Map.of(
                "order", order,
                "descrizione", descrizioneScenario,
                "tipo-scelta", tipoScelta,
                "oggetto", oggetto);

        storyManagerModel.addScenario(idStoria, idScenario, scenarioMap);
       
        if ("indovinello".equalsIgnoreCase(tipoScelta)) {
            Map<String, Object> indovinelloMap = safeCastToMap(scenarioData.get("extraFields"), "Dettagli indovinello non validi.");
            createIndovinello(idStoria, idScenario, indovinelloMap, storyManagerModel);
        } else if ("oggetto".equalsIgnoreCase(tipoScelta)) {
            Map<String, Object> oggettoMap = safeCastToMap(scenarioData.get("extraFields"), "Dettagli oggetto non validi.");
            createSceltaOggetto(idStoria, idScenario, oggettoMap, storyManagerModel);
        }
    }
    
    private void createIndovinello(String idStoria, String idScenario, Map<String, Object> indovinelloData, StoryManagerModel storyManagerModel)
            throws Exception {
        Logger logger = LoggerFactory.getLogger(StoryManager.class);

        String riddleText = extractString(indovinelloData, "riddleText");
        String riddleAnswer = extractString(indovinelloData, "riddleAnswer");
        String correctNext = extractString(indovinelloData, "correctNext");
        String wrongNext = extractString(indovinelloData, "wrongNext");

        logger.debug(
                "Creazione scenario. testo: '{}', risposta: '{}', prox-scenario-corretto: '{}', prox-scenario-errato: '{}'",
                riddleText, riddleAnswer, correctNext, wrongNext);

        Map<String, Object> data = Map.of(
                "testo", riddleText,
                "risposta", riddleAnswer,
                "prox-scenario-errato", correctNext,
                "prox-scenario-corretto", wrongNext);

        storyManagerModel.addIndovinello(idStoria, idScenario, data);
    }
    
    private void createSceltaOggetto(String idStoria, String idScenario, Map<String, Object> oggettoData, StoryManagerModel storyManagerModel)
            throws Exception {
        Logger logger = LoggerFactory.getLogger(StoryManager.class);

        String objectName = extractString(oggettoData, "objectName");
        String correctNext = extractString(oggettoData, "correctNext");
        String wrongNext = extractString(oggettoData, "wrongNext");

        logger.debug("Creazione scenario. testo: '{}', prox-scenario-corretto: '{}', prox-scenario-errato: '{}'",
                objectName, correctNext, wrongNext);

        Map<String, Object> data = Map.of(
                "nome-oggetto", objectName,
                "prox-scenario-errato", correctNext,
                "prox-scenario-corretto", wrongNext);

        storyManagerModel.addOggetto(idStoria, idScenario, data);
    }
    
    @PutMapping("/{id}/modify")
    public ResponseEntity<String> updateStory(@PathVariable("id") String storyId,
            @RequestBody Map<String, Object> storyData) {
        logger.debug("Richiesta per aggiornare la storia con ID: {}", storyId);
        try {
            StoryManagerModel storyManagerModel = new StoryManagerModel();

            // Aggiorna i dettagli principali della storia
            updateStoryDetails(storyId, storyData, storyManagerModel);

            // Aggiorna gli scenari della storia
            updateScenariosForStory(storyId, storyData, storyManagerModel);

            logger.debug("Modifiche salvate con successo per la storia con ID: {}", storyId);
            return ResponseEntity.ok("Storia e scenari aggiornati con successo.");
        } catch (IllegalArgumentException e) {
            logger.warn("Errore di validazione durante l'aggiornamento: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Errore durante l'aggiornamento della storia e degli scenari", e);
            return ResponseEntity.status(500).body("Errore durante l'aggiornamento della storia e degli scenari.");
        }
    }
    
    private void updateStoryDetails(String storyId, Map<String, Object> storyData, StoryManagerModel storyManagerModel)
            throws Exception {
        logger.debug("Aggiornamento dei dettagli principali della storia con ID: {}", storyId);

        String userid = extractString(storyData, "user-id");
        String titolo = extractString(storyData, "titolo");
        String descrizione = extractString(storyData, "descrizione");

        logger.debug("Titolo: '{}', Descrizione: '{}'", titolo, descrizione);

        Map<String, Object> storyMap = Map.of(
                "user-id", userid,
                "titolo", titolo,
                "descrizione", descrizione);

        storyManagerModel.updateStory(storyId, storyMap);
        logger.debug("Dettagli principali aggiornati per la storia con ID: {}", storyId);
    }

    private void updateScenariosForStory(String storyId, Map<String, Object> storyData,
            StoryManagerModel storyManagerModel) throws Exception {

        logger.debug("Modifica dei scenari con i dati: {}", storyData);
        List<?> scenariosList = (List<?>) storyData.get("scenari");
        logger.debug("Numero di scenari da aggiornare: {}", scenariosList.size());

        for (Object scenarioObj : scenariosList) {
            Map<String, Object> scenarioData = safeCastToMap(scenarioObj, "Scenario in formato non valido.");
            updateScenario(storyId, scenarioData, storyManagerModel);
        }
    }
    
    private void updateScenario(String storyId, Map<String, Object> scenarioData, StoryManagerModel storyManagerModel) throws Exception {
        String scenarioId = extractString(scenarioData, "idScenario");
        int order = extractInteger(scenarioData, "order");
        String descrizioneScenario = extractString(scenarioData, "descrizione");
        String tipoScelta = extractString(scenarioData, "tipo-scelta");
        String oggetto = extractString(scenarioData, "oggetto");
    
        logger.debug("Aggiornamento scenario. ID: {}, Ordine: {}, Descrizione: '{}', Tipo Scelta: '{}', Oggetto: '{}'",
            scenarioId, order, descrizioneScenario, tipoScelta, oggetto);
    
        Map<String, Object> scenarioMap = Map.of(
            "order", order,
            "descrizione", descrizioneScenario,
            "tipo-scelta", tipoScelta,
            "oggetto", oggetto
        );
    
        storyManagerModel.updateScenario(storyId, scenarioId, scenarioMap);
    
        if ("indovinello".equalsIgnoreCase(tipoScelta)) {
            updateIndovinello(storyId, scenarioId, scenarioData, storyManagerModel);
        } else if ("oggetto".equalsIgnoreCase(tipoScelta)) {
            updateOggetto(storyId, scenarioId, scenarioData, storyManagerModel);
        }
    }
  
    private void updateIndovinello(String storyId, String scenarioId, Map<String, Object> scenarioData,
            StoryManagerModel storyManagerModel) throws Exception {
        Map<String, Object> indovinelloData = safeCastToMap(scenarioData.get("extraFields"),
                "Dettagli indovinello non validi.");

        String testo = extractString(indovinelloData, "testo");
        String risposta = extractString(indovinelloData, "risposta");
        String proxScenarioCorretto = extractString(indovinelloData, "prox-scenario-corretto");
        String proxScenarioErrato = extractString(indovinelloData, "prox-scenario-errato");

        logger.debug(
                "Aggiornamento indovinello. Testo: '{}', Risposta: '{}', Prossimo Corretto: '{}', Prossimo Errato: '{}'",
                testo, risposta, proxScenarioCorretto, proxScenarioErrato);

        Map<String, Object> indovinelloMap = Map.of(
                "testo", testo,
                "risposta", risposta,
                "prox-scenario-corretto", proxScenarioCorretto,
                "prox-scenario-errato", proxScenarioErrato);

        storyManagerModel.updateIndovinello(storyId, scenarioId, indovinelloMap);
    }
    
    private void updateOggetto(String storyId, String scenarioId, Map<String, Object> scenarioData, StoryManagerModel storyManagerModel) throws Exception {
        Map<String, Object> oggettoData = safeCastToMap(scenarioData.get("extraFields"), "Dettagli oggetto non validi.");
    
        String nomeOggetto = extractString(oggettoData, "nome-oggetto");
        String proxScenarioCorretto = extractString(oggettoData, "prox-scenario-corretto");
        String proxScenarioErrato = extractString(oggettoData, "prox-scenario-errato");
    
        logger.debug("Aggiornamento oggetto. Nome: '{}', Prossimo Corretto: '{}', Prossimo Errato: '{}'",
            nomeOggetto, proxScenarioCorretto, proxScenarioErrato);
    
        Map<String, Object> oggettoMap = Map.of(
            "nome-oggetto", nomeOggetto,
            "prox-scenario-corretto", proxScenarioCorretto,
            "prox-scenario-errato", proxScenarioErrato
        );
    
        storyManagerModel.updateOggetto(storyId, scenarioId, oggettoMap);
    }
   
    private String extractString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value instanceof String && !((String) value).isEmpty()) {
            return (String) value;
        }
        return "";
    }

    private int extractInteger(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value instanceof String && !((String) value).isEmpty()) {
            return Integer.parseInt((String) value);
        }
        return 0;
    }

    private Map<String, Object> safeCastToMap(Object obj, String errorMessage) {
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> rawMap = (Map<?, ?>) obj;
            Map<String, Object> castedMap = new HashMap<>();
            
            for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                if (entry.getKey() instanceof String) {
                    castedMap.put((String) entry.getKey(), entry.getValue());
                } else {
                    throw new IllegalArgumentException("Chiave non valida nella mappa: atteso String.");
                }
            }
            
            return castedMap;
        }
        throw new IllegalArgumentException(errorMessage);
    }
    
}
