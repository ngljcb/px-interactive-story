// Importa i moduli Firebase necessari
import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.15.0/firebase-app.js';
import { getAuth } from 'https://www.gstatic.com/firebasejs/9.15.0/firebase-auth.js';
import { getFirestore } from 'https://www.gstatic.com/firebasejs/9.15.0/firebase-firestore.js';

// Configurazione Firebase
const firebaseConfig = {
  apiKey: 'AIzaSyCoStn0bNdw2hKU40H_n_i__rIzvPyQqGo',
  authDomain: 'interactive-story-8b0da.firebaseapp.com',
  projectId: 'interactive-story-8b0da',
  storageBucket: 'interactive-story-8b0da.firebasestorage.app',
  messagingSenderId: '957954197967',
  appId: '1:957954197967:web:951461dff9de302ff56aba',
};

// Inizializza Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);

// Esporta il modulo di autenticazione
export { auth, db };
