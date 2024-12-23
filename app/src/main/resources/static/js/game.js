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

  // Salva lo storyId e lo scenarioId su Firebase
  async function saveProgress(storyId, scenarioId) {
    try {
      const payload = {
        storyId: storyId,
        scenarioId: scenarioId,
      };

      const response = await fetch(`${baseUrl}/save-progress`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`2. Errore durante il salvataggio del progresso: ${response.status} - ${errorText}`);
      }

      console.log('Progresso salvato con successo:', await response.text());
    } catch (error) {
      console.error('3. Errore durante il salvataggio del progresso:', error.message);
    }
  }

  // Aggiungi un event listener al pulsante "Home"
  const homeButton = document.getElementById('home-button'); // Seleziona il pulsante con id "home-button"
  if (homeButton) {
    homeButton.addEventListener('click', async (event) => {
      event.preventDefault(); // Evita il comportamento predefinito del pulsante

      // Verifica se esiste uno scenario corrente
      if (!currentScenario) {
        console.warn('Scenario corrente non trovato. Nessun progresso da salvare.');
        //window.location.href = '/index?auth=true'; // Reindirizza comunque alla Home
        return;
      }

      const storyId = getStoryIdFromQuery(); // Recupera lo storyId dalla query string
      const scenarioId = currentScenario.id; // Presuppone che currentScenario abbia uno scenarioId

      console.log('currentScenario:', currentScenario);
      console.log('storyId:', storyId);
      console.log('scenarioId:', scenarioId);

      try {
        await saveProgress(storyId, scenarioId); // Salva lo storyId e lo scenarioId su Firebase
        console.log('Progresso salvato con successo.');
      } catch (error) {
        console.error('1. Errore durante il salvataggio del progresso:', error.message);
      }

      // Reindirizza alla Home
      window.location.href = '/index?auth=true';
    });
  } else {
    console.error('Pulsante Home non trovato nel DOM.');
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
        window.location.href = '/login';
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

      const payload = { risposta: answer };
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
    const homeBtn = document.getElementById('home-button');
    const endgameBtn = document.getElementById('endgame');

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

        // Aggiungi un <select> dinamico per l'inventario
        const inventorySelect = document.createElement('select');
        inventorySelect.id = 'inventory-select';
        inventorySelect.required = true;

        const option = document.createElement('option');
        option.value = 'default-wrong-answer';
        option.textContent = 'Non ho questo oggetto';
        inventorySelect.appendChild(option);

        // Popola il <select> con gli oggetti dell'inventario
        const inventory = currentScenario.inventario || []; // Usa l'inventario dal backend
        if (inventory.length > 0) {
          inventory.forEach((item) => {
            const option = document.createElement('option');
            option.value = item;
            option.textContent = item;
            inventorySelect.appendChild(option);
          });
        }

        // Modifica il form per includere il <select> invece dell'<input>
        answerForm.innerHTML = ''; // Svuota il contenuto del form
        answerForm.appendChild(inventorySelect);

        // Aggiunge il pulsante "Invia"
        const submitButton = document.createElement('button');
        submitButton.type = 'submit';
        submitButton.className = 'btn-submit';
        submitButton.textContent = 'Invia';
        answerForm.appendChild(submitButton);

        // Gestisce la risposta tramite il select
        answerForm.onsubmit = (e) => {
          e.preventDefault();
          const answer = inventorySelect.value; // Ottieni l'oggetto selezionato
          playTurn(answer);
        };
      } else {
        dynamicContent.style.display = 'none';
      }
    } else {
      dynamicContent.style.display = 'none';
    }

    // Mostra il form per l'indovinello o oggetto
    if (currentScenario.scelte) {
      answerForm.style.display = 'block';
      endgameBtn.style.display = 'none';

      if (currentScenario.tipoScelta !== 'oggetto') {
        // Ripristina il form originale per input testo
        answerForm.innerHTML = `
        <input type="text" id="answer" placeholder="Inserisci la tua risposta" required />
        <button type="submit" class="btn-submit">Invia</button>
      `;
        answerForm.onsubmit = (e) => {
          e.preventDefault();
          const answer = document.getElementById('answer').value.trim();
          playTurn(answer);
        };
      }
    } else {
      // Scenario finale
      choicesContainer.innerHTML = `<p>Congratulazioni! Hai completato la storia!</p>`;
      answerForm.style.display = 'none';
      homeBtn.style.display = 'none';
      endgameBtn.style.display = 'block';

      const payload = {};
      const response = fetch(`${baseUrl}/end`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      });
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
