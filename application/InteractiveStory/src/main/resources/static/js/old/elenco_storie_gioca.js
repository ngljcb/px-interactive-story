const stories = ["Storia 1", "Storia 2", "Storia 3", "Storia 4", "Storia 5"];
const storyGrid = document.getElementById('story-grid');
stories.forEach((story, index) => {
    const storyCard = document.createElement('div');
    storyCard.className = 'story-card';
    const storyTitle = document.createElement('h2');
    storyTitle.textContent = story;
    const storyDescription = document.createElement('p');
    storyDescription.textContent = `Nome ${story.toLowerCase()}`;
    const storyButton = document.createElement('a');
    //storyButton.href = `storia${index + 1}.html`;
    storyButton.className = 'story-button';
    storyButton.textContent = 'Gioca';
    storyCard.appendChild(storyTitle);
    storyCard.appendChild(storyDescription);
    storyCard.appendChild(storyButton);
    storyGrid.appendChild(storyCard);
});
