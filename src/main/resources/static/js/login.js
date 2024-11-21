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
      return userCredential.user.getIdToken(); // Ottiene il token JWT
    })
    .then((idToken) => {
      console.log('Generated Token:', idToken); // Debug
      alert('Login successful');
      window.location.href = '/'; // Reindirizza alla pagina index
    })
    .catch((error) => {
      console.error('Login Error:', error.message);

      // Gestione degli errori di Firebase
      if (error.code === 'auth/user-not-found') {
        alert('No user found with this email.');
      } else if (error.code === 'auth/wrong-password') {
        alert('Incorrect password.');
      } else if (error.code === 'auth/invalid-email') {
        alert('Invalid email address.');
      } else {
        alert('Login failed: ' + error.message);
      }
    });
});
