document.addEventListener('DOMContentLoaded', async () => {
  const storyContainer = document.getElementById('stories-container');

  // Funzione per recuperare le storie dal backend
  async function fetchStories() {
    try {
      const response = await fetch('/api/stories');
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

  // Funzione per creare un box per ogni storia
  function createStoryCard(story) {
    const card = document.createElement('div');
    card.className = 'story-card';

    const title = document.createElement('h2');
    title.textContent = story.title;

    const description = document.createElement('p');
    description.textContent = story.description;

    const button = document.createElement('button');
    button.textContent = 'Gioca ora';
    button.onclick = async () => {
      try {
        const user = await isUserLoggedIn();
        if (user) {
          // Utente loggato: reindirizza al gioco
          window.location.href = `/game?storyId=${story.id}`;
        } else {
          // Utente non loggato: reindirizza al login
          alert('Devi essere loggato per giocare.');
          window.location.href = `/login?storyId=${story.id}`;
        }
      } catch (error) {
        console.error('Errore durante la gestione del click sul pulsante:', error.message);
        alert('Si è verificato un errore. Riprova più tardi.');
      }
    };

    card.appendChild(title);
    card.appendChild(description);
    card.appendChild(button);
    return card;
  }

  try {
    // Carica le storie dal backend
    const stories = await fetchStories();

    // Mostra le storie
    if (stories.length === 0) {
      storyContainer.innerHTML = `<p>Nessuna storia disponibile al momento.</p>`;
    } else {
      stories.forEach((story) => {
        const storyCard = createStoryCard(story);
        storyContainer.appendChild(storyCard);
      });
    }
  } catch (error) {
    console.error('Errore durante il caricamento delle storie:', error.message);
    storyContainer.innerHTML = `<p>Errore durante il caricamento delle storie. Riprova più tardi.</p>`;
  }
});
