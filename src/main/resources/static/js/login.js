document.getElementById('loginForm').addEventListener('submit', function (e) {
  e.preventDefault();
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  firebase
    .auth()
    .signInWithEmailAndPassword(email, password)
    .then((userCredential) => {
      userCredential.user.getIdToken().then((idToken) => {
        fetch('http://localhost:8080/auth/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + idToken,
          },
          body: JSON.stringify({ email, password }),
        }).then((response) => {
          if (response.ok) {
            alert('Login successful');
            // Naviga alla home o a un'altra pagina
          } else {
            alert('Login failed');
          }
        });
      });
    })
    .catch((error) => {
      alert(error.message);
    });
});
