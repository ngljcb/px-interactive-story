document.addEventListener('DOMContentLoaded', async () => {
  const baseUrl = '/api/game'; // Base URL del backend
  let currentScenario = null;

  // Recupera lo storyId dalla query string
  function getStoryIdFromQuery() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('storyId');
  }

  // Recupera lo userId dalla sessione lato backend
  async function getUserIdFromSession() {
    try {
      const response = await fetch('/session/user'); // Endpoint per ottenere l'utente dalla sessione
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Errore nel recupero dello userId: ${response.status} - ${errorText}`);
      }

      const user = await response.json();
      return user.uid; // Assumendo che `uid` sia la proprietà dello userId
    } catch (error) {
      console.error('Errore nel recupero dello userId dalla sessione:', error.message);
      return null;
    }
  }

  // Esegui il setup della partita
  async function setupGame(userId, storyId) {
    try {
      if (!userId || !storyId) {
        throw new Error('userId o storyId non valido.');
      }

      const payload = {
        idGiocatore: userId,
        idStoria: storyId,
      };

      const response = await fetch(`${baseUrl}/setup`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Errore durante il setup: ${response.status} - ${errorText}`);
      }

      console.log('Setup completato:', await response.text());
      await loadCurrentScenario();
    } catch (error) {
      console.error('Errore nel setup:', error.message);

      // Reindirizza alla pagina di login se l'utente non è loggato
      if (error.message.includes('userId non valido')) {
        alert('Non sei loggato. Verrai reindirizzato alla pagina di login.');
        window.location.href = '/login.html';
      }
    }
  }

  // Carica lo scenario corrente
  async function loadCurrentScenario() {
    try {
      const response = await fetch(`${baseUrl}/current-scenario`);
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Errore HTTP: ${response.status} - ${errorText}`);
      }

      currentScenario = await response.json();
      console.log('Scenario corrente:', currentScenario);

      // Mostra un alert se un oggetto è stato trovato
      if (currentScenario.oggettotrovato) {
        alert(`Complimenti! Hai trovato "${currentScenario.oggettotrovato}" durante il percorso. È stato aggiunto al tuo inventario!`);
      }

      renderScenario();
    } catch (error) {
      console.error('Errore durante il caricamento dello scenario corrente:', error.message);
    }
  }

  // Gioca un turno
  async function playTurn(answer) {
    console.log('risposta:', answer);
    try {
      if (!answer) {
        console.warn('Risposta non fornita.');
        return;
      }

      const payload = {
        risposta: answer,
      };

      const response = await fetch(`${baseUrl}/play`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Errore durante il gameplay: ${response.status} - ${errorText}`);
      }

      const result = await response.text();
      console.log('Risultato del turno:', result);

      // Carica lo scenario aggiornato
      await loadCurrentScenario();
    } catch (error) {
      console.error('Errore durante il gameplay:', error.message);
    }
  }

  // Mostra lo scenario corrente
  function renderScenario() {
    if (!currentScenario) {
      console.warn('Nessuno scenario corrente trovato.');
      return;
    }

    const scenarioTitle = document.getElementById('scenario-title');
    const scenarioDescription = document.getElementById('scenario-description');
    const choicesContainer = document.getElementById('choices-container');
    const dynamicContent = document.getElementById('special-content'); // Sezione dinamica
    const answerForm = document.getElementById('answer-form');
    const answerInput = document.getElementById('answer'); // Textbox per la risposta

    scenarioTitle.textContent = `Scenario ${currentScenario.ordernumber}`;
    scenarioDescription.textContent = currentScenario.descrizione;
    choicesContainer.innerHTML = ''; // Pulisce le scelte precedenti

    console.log('dati scenario:', currentScenario);

    // Svuota il textbox `answer` e mostra solo il placeholder
    if (answerInput) {
      answerInput.value = ''; // Svuota il textbox
    }

    // Gestisce contenuto dinamico in base a `tipo-scelta`
    if (currentScenario.tipoScelta) {
      dynamicContent.style.display = 'block';
      if (currentScenario.tipoScelta === 'indovinello') {
        dynamicContent.textContent = `Indovinello: ${currentScenario.testodamostrare}`;
      } else if (currentScenario.tipoScelta === 'oggetto') {
        dynamicContent.textContent = `Oggetto richiesto: ${currentScenario.testodamostrare}`;
      } else {
        dynamicContent.style.display = 'none';
      }
    } else {
      dynamicContent.style.display = 'none';
    }

    // Mostra il form per l'indovinello o oggetto
    if (currentScenario.scelte) {
      answerForm.style.display = 'block';

      answerForm.onsubmit = (e) => {
        e.preventDefault();
        const answer = document.getElementById('answer').value.trim().toLowerCase();
        playTurn(answer);
      };
    } else {
      // Scenario finale
      choicesContainer.innerHTML = '<p>Congratulazioni! Hai completato la storia!</p>';
      answerForm.style.display = 'none';
    }
  }

  // Recupera lo storyId dalla query string
  const storyId = getStoryIdFromQuery();
  if (!storyId) {
    console.error('Story ID non trovato nella query string.');
    alert('ID della storia non valido. Torna alla pagina precedente.');
    window.location.href = '/';
    return;
  }

  // Recupera lo userId dalla sessione e avvia il setup
  const userId = await getUserIdFromSession();
  if (!userId) {
    console.error('User ID non trovato nella sessione. Reindirizzamento alla pagina di login.');
    alert('Non sei loggato. Verrai reindirizzato alla pagina di login.');
    window.location.href = '/login';
    return;
  }

  await setupGame(userId, storyId);
});
