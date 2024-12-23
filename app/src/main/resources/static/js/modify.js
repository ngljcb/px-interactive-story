document.addEventListener('DOMContentLoaded', () => {
  const scenariosContainer = document.getElementById('scenarios-container');
  const addScenarioButton = document.getElementById('add-scenario-button');
  const saveStoryButton = document.getElementById('save-story-button');

  const urlParams = new URLSearchParams(window.location.search);
  const storyId = urlParams.get('id'); // Ottiene l'ID della storia dalla query string
  let scenarioCount = 0;

  if (!storyId) {
    alert('ID storia non trovato. Torna alla pagina precedente.');
    window.location.href = '/settings';
    return;
  }

  // Funzione per aggiungere una nuova sezione scenario
  addScenarioButton.addEventListener('click', () => {
    scenarioCount++;
    const scenarioData = {
      order: scenarioCount,
      descrizione: '',
      'tipo-scelta': '',
      oggetto: '',
    };
    addScenario(scenarioData);
  });

  // Carica i dati della storia
  async function loadStoryData() {
    try {
      const response = await fetch(`/api/stories/${storyId}/modify`);
      if (!response.ok) {
        throw new Error('Errore durante il caricamento della storia.');
      }

      const storyData = await response.json();
      document.getElementById('idscrittore').value = storyData.idscrittore;
      document.getElementById('story-title').value = storyData.titolo;
      document.getElementById('story-description').value = storyData.descrizione;

      storyData.Scenari.forEach((scenario) => addScenario(scenario));
    } catch (error) {
      console.error('Errore durante il caricamento della storia:', error.message);
      alert('Errore durante il caricamento della storia.');
    }
  }

  // Aggiunge una nuova sezione scenario (con dati opzionali)
  function addScenario(data = {}) {
    scenarioCount++;

    const scenarioSection = document.createElement('div');
    scenarioSection.classList.add('scenario-section');
    scenarioSection.setAttribute('data-scenario-id', scenarioCount);

    scenarioSection.innerHTML = `
          <h3>Scenario ${scenarioCount}</h3>
          <label for="order-${scenarioCount}">Ordine:</label>
          <input type="number" id="order-${scenarioCount}" value="${data.order || ''}" placeholder="Ordine scenario" required>
          
          <label for="description-${scenarioCount}">Descrizione:</label>
          <textarea id="description-${scenarioCount}" placeholder="Descrizione scenario" required>${data.descrizione || ''}</textarea>
          
          <label for="choice-type-${scenarioCount}">Tipo Scelta:</label>
          <select id="choice-type-${scenarioCount}" required>
            <option value="" disabled ${!data['tipo-scelta'] ? 'selected' : ''}>Seleziona tipo</option>
            <option value="indovinello" ${data['tipo-scelta'] === 'indovinello' ? 'selected' : ''}>Indovinello</option>
            <option value="oggetto" ${data['tipo-scelta'] === 'oggetto' ? 'selected' : ''}>Oggetto</option>
          </select>
          
          <div id="extra-fields-${scenarioCount}" style="display: none;"></div>
          
          <label for="found-object-${scenarioCount}">Oggetto Trovato:</label>
          <input type="text" id="found-object-${scenarioCount}" value="${data.oggetto || ''}" placeholder="Oggetto trovato nello scenario">
        `;

    scenariosContainer.appendChild(scenarioSection);

    const choiceTypeSelect = scenarioSection.querySelector(`#choice-type-${scenarioCount}`);
    const extraFieldsContainer = scenarioSection.querySelector(`#extra-fields-${scenarioCount}`);

    choiceTypeSelect.addEventListener('change', () => {
      renderExtraFields(choiceTypeSelect, extraFieldsContainer, data);
    });

    if (data['tipo-scelta']) {
      renderExtraFields(choiceTypeSelect, extraFieldsContainer, data);
    }
  }

  // Mostra i campi extra in base al tipo di scelta
  function renderExtraFields(selectElement, container, data = {}) {
    const choiceType = selectElement.value;
    container.innerHTML = '';

    if (choiceType === 'indovinello') {
      container.innerHTML = `
            <label for="riddle-text-${scenarioCount}">Testo Indovinello:</label>
            <input type="text" id="riddle-text-${scenarioCount}" value="${data.Indovinello?.testo || ''}" placeholder="Testo indovinello" required>
  
            <label for="riddle-answer-${scenarioCount}">Risposta:</label>
            <input type="text" id="riddle-answer-${scenarioCount}" value="${data.Indovinello?.risposta || ''}" placeholder="Risposta" required>
  
            <label for="correct-next-${scenarioCount}">Prossimo Scenario Corretto:</label>
            <input type="text" id="correct-next-${scenarioCount}" value="${
        data.Indovinello?.['prox-scenario-corretto'] || ''
      }" placeholder="Prossimo Scenario Corretto" required>
  
            <label for="wrong-next-${scenarioCount}">Prossimo Scenario Errato:</label>
            <input type="text" id="wrong-next-${scenarioCount}" value="${
        data.Indovinello?.['prox-scenario-errato'] || ''
      }" placeholder="Prossimo Scenario Errato" required>
          `;
    } else if (choiceType === 'oggetto') {
      container.innerHTML = `
            <label for="object-name-${scenarioCount}">Nome Oggetto:</label>
            <input type="text" id="object-name-${scenarioCount}" value="${data.Oggetto?.['nome-oggetto'] || ''}" placeholder="Nome oggetto" required>
  
            <label for="correct-next-${scenarioCount}">Prossimo Scenario Corretto:</label>
            <input type="text" id="correct-next-${scenarioCount}" value="${
        data.Oggetto?.['prox-scenario-corretto'] || ''
      }" placeholder="Prossimo Scenario Corretto" required>
  
            <label for="wrong-next-${scenarioCount}">Prossimo Scenario Errato:</label>
            <input type="text" id="wrong-next-${scenarioCount}" value="${
        data.Oggetto?.['prox-scenario-errato'] || ''
      }" placeholder="Prossimo Scenario Errato" required>
          `;
    }

    container.style.display = 'block';
  }

  // Salvataggio delle modifiche alla storia
  saveStoryButton.addEventListener('click', async () => {
    const idscrittore = document.getElementById('idscrittore').value;
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
          testo: document.getElementById(`riddle-text-${id}`).value,
          risposta: document.getElementById(`riddle-answer-${id}`).value,
          'prox-scenario-corretto': document.getElementById(`correct-next-${id}`).value,
          'prox-scenario-errato': document.getElementById(`wrong-next-${id}`).value,
        };
      } else if (choiceType === 'oggetto') {
        extraFields = {
          'nome-oggetto': document.getElementById(`object-name-${id}`).value,
          'prox-scenario-corretto': document.getElementById(`correct-next-${id}`).value,
          'prox-scenario-errato': document.getElementById(`wrong-next-${id}`).value,
        };
      }

      return {
        idScenario: id,
        order: order,
        descrizione: scenarioDescription,
        'tipo-scelta': choiceType,
        oggetto: foundObject,
        extraFields,
      };
    });

    const storyData = {
      id: storyId,
      titolo: title,
      descrizione: description,
      'user-id': idscrittore,
      scenari: scenarios,
    };

    try {
      const response = await fetch(`/api/stories/${storyId}/modify`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(storyData),
      });

      if (!response.ok) {
        const errorDetails = await response.text();
        throw new Error(`Errore durante il salvataggio della storia: ${response.status} - ${errorDetails}`);
      }

      alert('Modifiche salvate con successo!');
      window.location.href = '/settings';
    } catch (error) {
      console.error('Errore:', error.message);
      alert('Si è verificato un errore durante il salvataggio delle modifiche. Riprova più tardi.');
    }
  });

  // Inizializzazione
  loadStoryData();
});
