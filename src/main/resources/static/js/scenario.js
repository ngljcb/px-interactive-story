const campoTestoGrande = document.querySelector('textarea'); // Campo di testo grande
const campoTestoPiccolo = document.querySelector('input[type="text"]:nth-of-type(1)'); // Primo campo di testo piccolo
const altroCampoTesto = document.querySelector('#caso2 input[type="text"]'); // Campo di testo nel secondo caso

const bottone1 = document.querySelector('#caso1 button:nth-of-type(1)'); // Primo bottone del caso 1
const bottone2 = document.querySelector('#caso1 button:nth-of-type(2)'); // Secondo bottone del caso 1
const bottoneUnico = document.querySelector('#caso2 button'); // Bottone del caso 2

// Variabile booleana per gestire il caso attivo
let isCaso1 = true; // true: mostra il caso 1, false: mostra il caso 2

// Funzione per mostrare il caso selezionato
const mostraCaso = (caso) => {
    if (caso === 1) {
        document.getElementById('caso1').style.display = 'block';
        document.getElementById('caso2').style.display = 'none';
        isCaso1 = true; // Aggiorna lo stato
    } else if (caso === 2) {
        document.getElementById('caso1').style.display = 'none';
        document.getElementById('caso2').style.display = 'block';
        isCaso1 = false; // Aggiorna lo stato
    }
};