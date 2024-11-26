import { auth } from './firebaseConfig.js';
import { createUserWithEmailAndPassword } from 'https://www.gstatic.com/firebasejs/9.15.0/firebase-auth.js';

let isSubmitting = false;

document.getElementById('registerForm').addEventListener('submit', function (e) {
  e.preventDefault();
  if (isSubmitting) return; // Evita submit multipli
  isSubmitting = true;

  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  console.log('Starting registration process for email:', email);

  // Registra l'utente tramite Firebase Authentication
  createUserWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      const user = userCredential.user; // Ottieni l'utente creato
      console.log('User registered successfully:', user);

      // Invia i dati al backend
      return fetch('/auth/firestore', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          uid: user.uid,
          email: user.email,
          username: email.split('@')[0],
          storyAdmin: false,
        }),
      });
    })
    .then((response) => {
      if (!response.ok) {
        return response.text().then((text) => {
          throw new Error(text);
        });
      }
      console.log('User data saved successfully to Firestore');

      // Controlla la query string per lo storyId
      const urlParams = new URLSearchParams(window.location.search);
      const storyId = urlParams.get('storyId');

      if (storyId) {
        // Se storyId è presente, reindirizza a /game con lo storyId come query string
        alert('Registration successful. Redirecting to the game...');
        window.location.href = `/game?storyId=${storyId}`;
      } else {
        // Altrimenti, reindirizza alla pagina di login
        alert('Registration successful. You can now log in.');
        window.location.href = '/login';
      }
    })
    .catch((error) => {
      console.error('Error during registration:', error.message);
      alert('Registration failed: ' + error.message);
    })
    .finally(() => {
      isSubmitting = false; // Sblocca il form
    });
});
