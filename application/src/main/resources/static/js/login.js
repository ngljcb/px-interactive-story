import { auth } from './firebaseConfig.js';
import { signInWithEmailAndPassword } from 'https://www.gstatic.com/firebasejs/9.15.0/firebase-auth.js';

document.getElementById('loginForm').addEventListener('submit', function (e) {
  e.preventDefault();
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  console.log('Starting login process for email:', email);

  // Autenticazione tramite Firebase
  signInWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      console.log('Login successful!');
      const userId = userCredential.user.uid; // Ottieni l'ID dell'utente
      const userEmail = userCredential.user.email;
      console.log('User ID:', userId); // Debug
      return userCredential.user.getIdToken().then((idToken) => ({ idToken, userId, userEmail })); // Ottieni il token JWT e l'ID
    })
    .then(({ idToken, userId, userEmail }) => {
      console.log('Generated Token:', idToken); // Debug

      // Effettua una chiamata per creare la sessione lato backend con idToken e uid
      return fetch('/session/saveUser', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include', // Invia cookie con la richiesta
        body: JSON.stringify({
          email: userEmail,
          username: userEmail.split('@')[0],
          userId: userId,
          idToken: idToken,
        }),
      });
    })
    .then((response) => {
      if (!response.ok) {
        return response.text().then((errorText) => {
          throw new Error(errorText || 'Errore durante la creazione della sessione');
        });
      }
      return response.text();
    })
    .then((message) => {
      console.log('Backend response:', message);
      alert('Login successful');

      // Controlla la query string per lo storyId
      const urlParams = new URLSearchParams(window.location.search);
      const storyId = urlParams.get('storyId');
      const settings = urlParams.get('settings');

      if (storyId) {
        // Se storyId è presente, reindirizza a /game con lo storyId come query string
        window.location.href = `/game?storyId=${storyId}`;
      } else if (settings) {
        // Se storyId è presente, reindirizza a /game con lo storyId come query string
        window.location.href = '/settings';
      } else {
        // Altrimenti, reindirizza alla home
        window.location.href = '/';
      }
    })
    .catch((error) => {
      console.error('Login Error:', error.message);

      // Gestione degli errori di Firebase
      if (error.code === 'auth/user-not-found') {
        alert('Utente non trovato. Verifica le credenziali.');
      } else if (error.code === 'auth/wrong-password') {
        alert('Password errata. Riprova.');
      } else if (error.code === 'auth/invalid-email') {
        alert('Indirizzo email non valido.');
      } else {
        alert('Errore: ' + error.message);
      }
    });
});
