const oggetti = ["ogg 1", "ogg 2", "ogg 3", "ogg 4"];
const scenari = ["Scenario 1", "Scenario 2", "Scenario 3", "Scenario 4"];
const selectOggetti = document.getElementById("oggetto");
const selectUtilizza = document.getElementById("scenario_utilizza");
const selectNonUtilizza = document.getElementById("scenario_non_utilizza");

function resetForm() 
{
    const form = document.querySelector("form");
    form.reset();
    selectUtilizza.disabled = false;
    selectNonUtilizza.disabled = false;
}

function popolaSelect() 
{
    //caricamento oggetti
    const defaultOptionOggetti = document.createElement("option");
    defaultOptionOggetti.value = "-1";
    defaultOptionOggetti.textContent = "Seleziona un oggetto";
    selectOggetti.appendChild(defaultOptionOggetti.cloneNode(true));
    scenari.forEach((oggetto) => {
        const option = document.createElement("option");
        option.value = oggetto;
        option.textContent = oggetto;
        selectOggetti.appendChild(option.cloneNode(true));
    });

    //caricamento scenari
    const defaultOptionScenari = document.createElement("option");
    defaultOptionScenari.value = "-1";
    defaultOptionScenari.textContent = "Seleziona uno scenario";
    selectUtilizza.appendChild(defaultOptionScenari.cloneNode(true));
    selectNonUtilizza.appendChild(defaultOptionScenari);
    scenari.forEach((scenario) => {
        const option = document.createElement("option");
        option.value = scenario;
        option.textContent = scenario;
        selectUtilizza.appendChild(option.cloneNode(true));
        selectNonUtilizza.appendChild(option);
    });
}

function raccogliDati() 
{
    const nomeScenario = document.getElementById("nome_scenario").value;
    const descrizioneScenario = document.getElementById("descrizione_scenario").value;
    const descrizionePreferenza = document.getElementById("descrizione_preferenza").value;
    const oggetto = document.getElementById("oggetto").value;
    const scenarioUtilizza = document.getElementById("scenario_utilizza").value;
    const scenarioNonUtilizza = document.getElementById("scenario_non_utilizza").value;
    if (!nomeScenario || !descrizioneScenario || !descrizionePreferenza)
    {
        alert("Tutti i campi devono essere compilati.");
        return;
    }
    else
    {
        const dati = {
            nomeScenario,
            descrizioneScenario,
            descrizionePreferenza,
            oggetto,
            scenarioUtilizza: scenarioUtilizza !== "-1" ? scenarioUtilizza : null,
            scenarioNonUtilizza: scenarioNonUtilizza !== "-1" ? scenarioNonUtilizza : null,
        };
        console.log("Dati raccolti:", dati);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    resetForm();
    popolaSelect();

    if (selectOggetti.value == "-1")
        selectUtilizza.disabled = true;

    selectOggetti.addEventListener("change", () => {
        if (selectOggetti.value !== "-1")
        {
            selectUtilizza.disabled = false;;
            selectNonUtilizza.disabled = true;
            selectNonUtilizza.value = "-1";
        }
        else
        {
            selectUtilizza.disabled = true;
            selectNonUtilizza.disabled = false;
            selectUtilizza.value = "-1";
        }
    });

    const bottoneInvio = document.querySelector("button[type='submit']");

    bottoneInvio.addEventListener("click", (e) => {
        e.preventDefault(); // Evita il refresh della pagina
        raccogliDati();
    });
});
