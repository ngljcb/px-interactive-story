package com.sweng.InteractiveStory.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseDBManager {

    private final Firestore firestore;

    public FirebaseDBManager() {
        this.firestore = FirestoreClient.getFirestore();
    }

    /**
     * Recupera un documento specifico da una collezione.
     *
     * @param collectionName Il nome della collezione.
     * @param documentId     L'ID del documento da recuperare.
     * @return DocumentSnapshot che rappresenta il documento richiesto.
     * @throws ExecutionException   In caso di errore durante l'esecuzione.
     * @throws InterruptedException In caso di interruzione del thread.
     */
    public DocumentSnapshot getDocument(String collectionName, String documentId)
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(collectionName).document(documentId);
        return docRef.get().get();
    }

    /**
     * Recupera un documento specifico dato il percorso completo.
     *
     * @param collectionPath Il percorso completo del documento.
     * @return DocumentSnapshot che rappresenta il documento richiesto.
     * @throws ExecutionException   In caso di errore durante l'esecuzione.
     * @throws InterruptedException In caso di interruzione del thread.
     */
    public DocumentSnapshot getDocument(String collectionPath) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.document(collectionPath);
        return docRef.get().get();
    }

    /**
     * Recupera una sottocollezione specifica.
     *
     * @param parentPath        Il percorso del documento genitore.
     * @param subCollectionName Il nome della sottocollezione.
     * @return QuerySnapshot che rappresenta i documenti nella sottocollezione.
     * @throws Exception In caso di errore durante il recupero.
     */
    public QuerySnapshot getSubCollection(String parentPath, String subCollectionName) throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection(parentPath).document().collection(subCollectionName).get();
        return future.get();
    }

    /**
     * Recupera tutti i documenti da una collezione specifica.
     *
     * @param collectionPath Il percorso della collezione.
     * @return QuerySnapshot che rappresenta i documenti nella collezione.
     * @throws Exception In caso di errore durante il recupero.
     */
    public QuerySnapshot getDocumentsFromCollection(String collectionPath) throws Exception {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionPath).get();
            return future.get();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero dei documenti dalla collezione: " + collectionPath, e);
        }
    }

    /**
     * Recupera tutti i documenti da una collezione specifica applicando una condizione where.
     *
     * @param collectionPath Il percorso della collezione.
     * @param key            La chiave su cui applicare la condizione where.
     * @param value          Il valore per la condizione where.
     * @return QuerySnapshot che rappresenta i documenti nella collezione che soddisfano la condizione.
     * @throws Exception In caso di errore durante il recupero.
     */
    public QuerySnapshot getDocsByCondition(String collectionPath, String key, String value) throws Exception {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionPath)
                                                        .whereEqualTo(key, value)
                                                        .get();
            return future.get();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero dei documenti dalla collezione: " + collectionPath +
                                 " con condizione where: " + key + " = " + value, e);
        }
    }

    /**
     * Elimina una collezione e tutti i documenti al suo interno.
     *
     * @param collectionPath Il percorso della collezione da eliminare.
     * @throws Exception In caso di errore durante l'eliminazione.
     */
    public void deleteCollection(String collectionPath) throws Exception {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionPath).get();
            QuerySnapshot querySnapshot = future.get();

            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                deleteDocument(collectionPath, document.getId());
            }

            System.out.println("Collezione eliminata: " + collectionPath);
        } catch (Exception e) {
            throw new Exception("Errore durante l'eliminazione della collezione: " + collectionPath, e);
        }
    }

    /**
     * Elimina un singolo documento da una collezione.
     *
     * @param collectionPath Il percorso della collezione.
     * @param documentId     L'ID del documento da eliminare.
     * @throws Exception In caso di errore durante l'eliminazione.
     */
    public void deleteDocument(String collectionPath, String documentId) throws Exception {
        try {
            DocumentReference docRef = firestore.collection(collectionPath).document(documentId);
            ApiFuture<WriteResult> writeResult = docRef.delete();
            writeResult.get();
            System.out.println("Documento eliminato: " + documentId);
        } catch (Exception e) {
            throw new Exception("Errore durante l'eliminazione del documento: " + documentId, e);
        }
    }

    /**
     * Aggiunge una collezione con un documento iniziale.
     *
     * @param collectionPath Il percorso della nuova collezione.
     * @param documentId     L'ID del documento iniziale.
     * @param data           I dati del documento iniziale.
     * @throws Exception In caso di errore durante l'aggiunta.
     */
    public void addCollection(String collectionPath, String documentId, Map<String, Object> data) throws Exception {
        try {
            DocumentReference docRef = firestore.collection(collectionPath).document(documentId);
            ApiFuture<WriteResult> writeResult = docRef.set(data);
            writeResult.get();
            System.out.println("Collezione creata: " + collectionPath + " con documento: " + documentId);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiunta della collezione: " + collectionPath, e);
        }
    }

    /**
     * Aggiunge un documento in una collezione esistente.
     *
     * @param collectionPath Il percorso della collezione.
     * @param documentId     L'ID del nuovo documento.
     * @param data           I dati del nuovo documento.
     * @throws Exception In caso di errore durante l'aggiunta.
     */
    public void addDocument(String collectionPath, String documentId, Map<String, Object> data) throws Exception {
        try {
            DocumentReference docRef = firestore.collection(collectionPath).document(documentId);
            ApiFuture<WriteResult> writeResult = docRef.set(data);
            writeResult.get();
            System.out.println("Documento aggiunto: " + documentId + " nella collezione: " + collectionPath);
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiunta del documento: " + documentId, e);
        }
    }

    /**
     * Modifica i dati di una collezione aggiungendo/modificando un documento iniziale.
     *
     * @param collectionPath Il percorso della collezione.
     * @param documentId     L'ID del documento da modificare.
     * @param data           I nuovi dati del documento.
     * @throws Exception In caso di errore durante la modifica.
     */
    public void modifyCollection(String collectionPath, String documentId, Map<String, Object> data) throws Exception {
        addCollection(collectionPath, documentId, data); // Riutilizza il metodo di aggiunta
        System.out.println("Collezione modificata: " + collectionPath + " con documento: " + documentId);
    }

    /**
     * Modifica i dati di un documento in una collezione esistente.
     *
     * @param collectionPath Il percorso della collezione.
     * @param documentId     L'ID del documento da modificare.
     * @param data           I nuovi dati del documento.
     * @throws Exception In caso di errore durante la modifica.
     */
    public void modifyDocument(String collectionPath, String documentId, Map<String, Object> data) throws Exception {
        addDocument(collectionPath, documentId, data); // Riutilizza il metodo di aggiunta
        System.out.println("Documento modificato: " + documentId + " nella collezione: " + collectionPath);
    }

    /**
     * Aggiunge un documento con un ID generato automaticamente.
     *
     * @param collectionPath Il percorso della collezione.
     * @param data           I dati del documento.
     * @return L'ID generato per il nuovo documento.
     * @throws Exception In caso di errore durante l'aggiunta.
     */
    public String addDocumentWithGeneratedId(String collectionPath, Map<String, Object> data) throws Exception {
        try {
            DocumentReference docRef = firestore.collection(collectionPath).document();
            docRef.set(data).get();
            System.out.println("Documento aggiunto con ID generato: " + docRef.getId());
            return docRef.getId();
        } catch (Exception e) {
            throw new Exception("Errore durante l'aggiunta del documento con ID generato nella collezione: " + collectionPath, e);
        }
    }
}
