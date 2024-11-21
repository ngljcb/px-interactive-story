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

  // Creazione dell'account su Firebase
  createUserWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      console.log('Account created successfully!');
      alert('Registration successful. You can now log in.');
      window.location.href = '/login'; // Reindirizza alla pagina di login
    })
    .catch((error) => {
      console.error('Firebase Error:', error.message);

      // Gestione degli errori di Firebase
      if (error.code === 'auth/email-already-in-use') {
        alert('The email address is already in use.');
      } else if (error.code === 'auth/weak-password') {
        alert('The password is too weak. Please use a stronger password.');
      } else if (error.code === 'auth/invalid-email') {
        alert('The email address is not valid.');
      } else {
        alert('Registration failed: ' + error.message);
      }
    })
    .finally(() => {
      isSubmitting = false; // Sblocca il form
    });
});
