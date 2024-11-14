document.getElementById('registerForm').addEventListener('submit', function (e) {
  e.preventDefault();
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  firebase
    .auth()
    .createUserWithEmailAndPassword(email, password)
    .then((userCredential) => {
      userCredential.user.getIdToken().then((idToken) => {
        fetch('http://localhost:8080/auth/register', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + idToken,
          },
          body: JSON.stringify({ email, password }),
        }).then((response) => {
          if (response.ok) {
            alert('Registration successful');
            window.location.href = 'login.html';
          } else {
            alert('Registration failed');
          }
        });
      });
    })
    .catch((error) => {
      alert(error.message);
    });
});
