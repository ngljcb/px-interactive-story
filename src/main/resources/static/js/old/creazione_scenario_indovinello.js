const scenari = ["Scenario 1", "Scenario 2", "Scenario 3", "Scenario 4"];
const selectRispostaCorretta = document.getElementById("scenario_risposta_corretta");
const selectRispostaSbagliata = document.getElementById("scenario_risposta_sbagliata");

function resetForm() {
    const form = document.querySelector("form");
    form.reset();
    selectRispostaCorretta.disabled = false;
    selectRispostaSbagliata.disabled = false;
}

function popolaSelect() {
    // Aggiunge un'opzione di default a entrambi i menu a tendina
    const defaultOption = document.createElement("option");
    defaultOption.value = "-1";
    defaultOption.textContent = "Seleziona uno scenario";
    selectRispostaCorretta.appendChild(defaultOption.cloneNode(true));
    selectRispostaSbagliata.appendChild(defaultOption);

    // Popola i menu a tendina con gli scenari
    scenari.forEach((scenario) => {
        const option = document.createElement("option");
        option.value = scenario;
        option.textContent = scenario;
        selectRispostaCorretta.appendChild(option.cloneNode(true));
        selectRispostaSbagliata.appendChild(option);
    });
}

function raccogliDati() {
    // Raccoglie i dati dai campi del modulo
    const nomeScenario = document.getElementById("nome_scenario").value;
    const descrizioneScenario = document.getElementById("descrizione_scenario").value;
    const descrizioneIndovinello = document.getElementById("descrizione_indovinello").value;
    const risposta = document.getElementById("risposta").value;
    const scenarioRispostaCorretta = document.getElementById("scenario_risposta_corretta").value;
    const scenarioRispostaSbagliata = document.getElementById("scenario_risposta_sbagliata").value;

    // Validazione
    if (!nomeScenario || !descrizioneScenario || !descrizioneIndovinello || !risposta) {
        alert("Tutti i campi devono essere compilati.");
        return;
    }

    // Crea un oggetto con i dati raccolti
    const dati = {
        nomeScenario,
        descrizioneScenario,
        descrizioneIndovinello,
        risposta,
        scenarioRispostaCorretta: scenarioRispostaCorretta !== "-1" ? scenarioRispostaCorretta : null,
        scenarioRispostaSbagliata: scenarioRispostaSbagliata !== "-1" ? scenarioRispostaSbagliata : null,
    };

    console.log("Dati raccolti:", dati);
}

document.addEventListener("DOMContentLoaded", () => {
    resetForm();
    popolaSelect();

    // Event listener per il bottone di invio
    const bottoneInvio = document.querySelector("button[type='submit']");
    bottoneInvio.addEventListener("click", (e) => {
        e.preventDefault(); // Previene il refresh della pagina
        raccogliDati();
    });
});
