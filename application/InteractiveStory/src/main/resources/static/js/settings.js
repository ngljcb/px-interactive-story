document.addEventListener('DOMContentLoaded', async () => {
  const storiesContainer = document.getElementById('stories-container');
  const addStoryButton = document.getElementById('add-story-button');

  // Funzione per verificare se l'utente è loggato
  async function isUserLoggedIn() {
    try {
      const response = await fetch('/session/user'); // Endpoint che restituisce i dati dell'utente loggato
      if (response.ok) {
        const user = await response.json();
        return user; // Restituisce i dati dell'utente se loggato
      }
      return null; // Utente non loggato
    } catch (error) {
      console.error('Errore durante la verifica dello stato di login:', error.message);
      return null;
    }
  }

  // Funzione per recuperare le storie dal backend
  async function fetchStories(user) {
    try {
      const response = await fetch(`/api/stories/${user}`);
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Errore nel caricamento delle storie: ${response.status} - ${errorText}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Errore durante il caricamento delle storie:', error.message);
      return [];
    }
  }

  // Funzione per creare un box per ogni storia
  function createStoryCard(story, userId) {
    const storyBox = document.createElement('div');
    storyBox.className = 'story-box';

    const title = document.createElement('h3');
    title.textContent = story.title;

    const modifyButton = document.createElement('button');
    modifyButton.textContent = 'Modifica';
    modifyButton.onclick = () => {
      window.location.href = `/modify?id=${story.id}`;
    };

    const deleteButton = document.createElement('button');
    deleteButton.textContent = 'Elimina';
    deleteButton.onclick = async () => {
      if (confirm(`Sei sicuro di voler eliminare "${story.title}"?`)) {
        await deleteStory(story.id, userId);
        loadStories(userId); // Ricarica le storie dopo l'eliminazione
      }
    };

    storyBox.appendChild(title);
    storyBox.appendChild(modifyButton);
    storyBox.appendChild(deleteButton);

    return storyBox;
  }

  // Funzione per creare un box per aggiungere una nuova storia
  function createAddStoryCard() {
    const storyBox = document.createElement('div');
    storyBox.className = 'story-box';

    const title = document.createElement('h3');
    title.textContent = 'Aggiungi una nuova storia';

    const addButton = document.createElement('button');
    addButton.textContent = 'Aggiungi';
    addButton.onclick = () => {
      window.location.href = '/create';
    };

    storyBox.appendChild(title);
    storyBox.appendChild(addButton);

    return storyBox;
  }

  // Funzione per mostrare i box delle storie
  function renderStories(stories, userId) {
    storiesContainer.innerHTML = ''; // Svuota il contenitore

    const addStoryCard = createAddStoryCard();
    storiesContainer.appendChild(addStoryCard);

    if (stories.length === 0) {
      return;
    }

    stories.forEach((story) => {
      const storyCard = createStoryCard(story, userId);
      storiesContainer.appendChild(storyCard);
    });
  }

  // Funzione per eliminare una storia
  async function deleteStory(storyId) {
    try {
      const response = await fetch(`/api/stories/${storyId}`, {
        method: 'DELETE',
      });
      if (!response.ok) {
        const errorDetails = await response.text();
        throw new Error(`Errore durante l'eliminazione della storia: ${response.status} - ${errorDetails}`);
      }
      alert('Storia eliminata con successo.');
      window.location.href = '/settings'; // Ricarica la pagina
    } catch (error) {
      console.error('Errore:', error.message);
      alert("Si è verificato un errore durante l'eliminazione della storia. Riprova più tardi.");
    }
  }

  // Verifica se l'utente è loggato e carica le storie
  try {
    const user = await isUserLoggedIn();
    if (!user) {
      console.log('Utente non loggato. Reindirizzamento alla pagina di login.');
      alert('Devi essere loggato per accedere a questa pagina.');
      window.location.href = '/login?settings=true';
      return;
    }

    // Carica le storie dal backend
    const stories = await fetchStories(user.uid);
    renderStories(stories, user.uid);
  } catch (error) {
    console.error('Errore durante il caricamento della pagina:', error.message);
    storiesContainer.innerHTML = '<p>Errore durante il caricamento delle storie. Riprova più tardi.</p>';
  }
});
