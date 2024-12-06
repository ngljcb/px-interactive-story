// create.js
document.addEventListener('DOMContentLoaded', () => {
  const scenariosContainer = document.getElementById('scenarios-container');
  const addScenarioButton = document.getElementById('add-scenario-button');
  const createStoryButton = document.getElementById('create-story-button');

  let scenarioCount = 0;

  // Recupera l'ID dell'utente loggato dalla sessione
  async function getUserId() {
    try {
      const response = await fetch('/session/user');
      if (!response.ok) {
        throw new Error("Impossibile recuperare l'utente loggato.");
      }
      const user = await response.json();
      return user.uid; // Assumendo che il campo `uid` rappresenti l'user ID
    } catch (error) {
      console.error("Errore durante il recupero dell'user-id:", error.message);
      return null;
    }
  }

  // Aggiunge una nuova sezione scenario
  function addScenario() {
    scenarioCount++;

    const scenarioSection = document.createElement('div');
    scenarioSection.classList.add('scenario-section');
    scenarioSection.setAttribute('data-scenario-id', scenarioCount);

    scenarioSection.innerHTML = `
        <h3>Scenario ${scenarioCount}</h3>
        <label for="order-${scenarioCount}">Ordine:</label>
        <input type="number" id="order-${scenarioCount}" placeholder="Ordine scenario" required>
        
        <label for="description-${scenarioCount}">Descrizione:</label>
        <textarea id="description-${scenarioCount}" placeholder="Descrizione scenario" required></textarea>
        
        <label for="choice-type-${scenarioCount}">Tipo Scelta:</label>
        <select id="choice-type-${scenarioCount}" required>
          <option value="" disabled selected>Seleziona tipo</option>
          <option value="indovinello">Indovinello</option>
          <option value="oggetto">Oggetto</option>
        </select>
        
        <div id="extra-fields-${scenarioCount}" style="display: none;"></div>
        
        <label for="found-object-${scenarioCount}">Oggetto Trovato:</label>
        <input type="text" id="found-object-${scenarioCount}" placeholder="Oggetto trovato nello scenario">
      `;

    scenariosContainer.appendChild(scenarioSection);

    const choiceTypeSelect = scenarioSection.querySelector(`#choice-type-${scenarioCount}`);
    const extraFieldsContainer = scenarioSection.querySelector(`#extra-fields-${scenarioCount}`);

    choiceTypeSelect.addEventListener('change', () => {
      extraFieldsContainer.innerHTML = '';
      if (choiceTypeSelect.value === 'indovinello') {
        extraFieldsContainer.innerHTML = `
            <label for="riddle-text-${scenarioCount}">Testo Indovinello:</label>
            <input type="text" id="riddle-text-${scenarioCount}" placeholder="Testo indovinello" required>
  
            <label for="riddle-answer-${scenarioCount}">Risposta:</label>
            <input type="text" id="riddle-answer-${scenarioCount}" placeholder="Risposta" required>
  
            <label for="correct-next-${scenarioCount}">Prossimo Scenario Corretto:</label>
            <input type="text" id="correct-next-${scenarioCount}" placeholder="Prossimo Scenario Corretto" required>
  
            <label for="wrong-next-${scenarioCount}">Prossimo Scenario Errato:</label>
            <input type="text" id="wrong-next-${scenarioCount}" placeholder="Prossimo Scenario Errato" required>
          `;
      } else if (choiceTypeSelect.value === 'oggetto') {
        extraFieldsContainer.innerHTML = `
            <label for="object-name-${scenarioCount}">Nome Oggetto:</label>
            <input type="text" id="object-name-${scenarioCount}" placeholder="Nome oggetto" required>
  
            <label for="correct-next-${scenarioCount}">Prossimo Scenario Corretto:</label>
            <input type="text" id="correct-next-${scenarioCount}" placeholder="Prossimo Scenario Corretto" required>
  
            <label for="wrong-next-${scenarioCount}">Prossimo Scenario Errato:</label>
            <input type="text" id="wrong-next-${scenarioCount}" placeholder="Prossimo Scenario Errato" required>
          `;
      }
      extraFieldsContainer.style.display = 'block';
    });
  }

  // Aggiungi scenario quando si preme il pulsante
  addScenarioButton.addEventListener('click', addScenario);

  // Creazione della storia
  createStoryButton.addEventListener('click', async () => {
    const title = document.getElementById('story-title').value;
    const description = document.getElementById('story-description').value;

    if (!title || !description) {
      alert('Compila tutti i campi per la storia!');
      return;
    }

    const scenarios = Array.from(scenariosContainer.querySelectorAll('.scenario-section')).map((section) => {
      const id = section.getAttribute('data-scenario-id');
      const order = document.getElementById(`order-${id}`).value;
      const scenarioDescription = document.getElementById(`description-${id}`).value;
      const choiceType = document.getElementById(`choice-type-${id}`).value;
      const foundObject = document.getElementById(`found-object-${id}`).value;

      let extraFields = {};
      if (choiceType === 'indovinello') {
        extraFields = {
          riddleText: document.getElementById(`riddle-text-${id}`).value,
          riddleAnswer: document.getElementById(`riddle-answer-${id}`).value,
          correctNext: document.getElementById(`correct-next-${id}`).value,
          wrongNext: document.getElementById(`wrong-next-${id}`).value,
        };
      } else if (choiceType === 'oggetto') {
        extraFields = {
          objectName: document.getElementById(`object-name-${id}`).value,
          correctNext: document.getElementById(`correct-next-${id}`).value,
          wrongNext: document.getElementById(`wrong-next-${id}`).value,
        };
      }

      return {
        order,
        description: scenarioDescription,
        choiceType,
        foundObject,
        extraFields,
      };
    });

    const storyData = {
      titolo: title,
      descrizione: description,
      scenari: scenarios,
    };

    try {
      // Recupera l'ID dell'utente loggato
      const userId = await getUserId();
      if (!userId) {
        alert("Impossibile recuperare i dati dell'utente. Effettua il login.");
        window.location.href = '/login';
        return;
      }

      // Aggiungi l'user-id ai dati della storia
      storyData['user-id'] = userId;

      const response = await fetch('/api/stories', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(storyData),
      });

      if (!response.ok) {
        const errorDetails = await response.text();
        throw new Error(`Errore durante la creazione della storia: ${response.status} - ${errorDetails}`);
      }

      alert('Storia e scenari creati con successo!');
      window.location.href = '/settings';
    } catch (error) {
      console.error('Errore:', error.message);
      alert('Si è verificato un errore durante la creazione della storia. Riprova più tardi.');
    }
  });
});
