// INITIALIZATION
let userSearch = "",
    categorie = "",
    filter = "buy",
    listArticles = [],
    countdowns = [];
const checkboxes = {
    saleIsOpen: false,
    isCurrentUser: false,
    saleIsWon: false,
    saleIsOnGoing: false,
    saleIsCreated: false,
    saleIsOver: false
};

init().then(() => {
    updateResults();
    displayFilters();
    updateCheckboxes();
});


// EVENTS

document.querySelector("#userSearch").oninput = $event => {
    userSearch = $event.target.value;
    updateResults();
}

document.querySelectorAll("input[type=radio]").forEach(radio => {
    radio.onchange = $event => {
        filter = $event.target.value;
        updateCheckboxes();
        updateResults();
    }
})

document.querySelector("#categorie").onchange = $event => {
    categorie = $event.target.value;
    updateResults();
}

for (let checkbox of Object.keys(checkboxes)) {
    document.querySelector(`#${checkbox}`).onclick = $event => {
        checkboxes[checkbox] = $event.target.checked;
        updateResults();
    }
}


// FUNCTIONS

async function search () {
    if (filter === "buy") {
        let {saleIsOpen, isCurrentUser, saleIsWon} = checkboxes;
        return await getArticlesToBuy(userSearch, categorie, saleIsOpen, isCurrentUser, saleIsWon);
    } else if  (filter === "sell") {
        let {saleIsOnGoing, saleIsCreated, saleIsOver} = checkboxes;
        return await getArticlesToSell(userSearch, categorie, saleIsOnGoing, saleIsCreated, saleIsOver);
    }
}

function updateResults() {
    clearAllCountdowns();
    document.querySelector("#root").innerHTML = "";
    search().then(articles => {
        listArticles = articles ? articles : [];
        listArticles.forEach(article => {
            createArticle(article)
        })
    });
}

function createArticle(article) {
    let container = document.createElement("div");
    container.classList.add("article");
    let title = document.createElement("h2");
    let description = document.createElement("h3");
    let prix = document.createElement("p");
    let endIn = getTimeUntil(getDate(article["dateFinEncheres"]));
    let countdown = document.createElement("p");
    let vendeur = document.createElement("p");
    title.textContent = article["nomArticle"];
    description.textContent = article["description"];
    prix.textContent = `Mise à prix: ${article["miseAPrix"]} point(s)`;
    countdown.textContent = getCountdownTextContent(endIn, false);
    vendeur.textContent = `Vendeur: ${article["vendeur"]["pseudo"]}`;
    container.appendChild(title);
    container.appendChild(description);
    container.appendChild(prix);
    container.appendChild(countdown);
    container.appendChild(vendeur);
    countdowns.push(
        setInterval(() => {
            countdown.textContent = getCountdownTextContent(endIn, true);
        }, 1000)
    );
    document.querySelector("#root").appendChild(container);
}

function getDate(date) {
    return new Date(date["year"], date["monthValue"] - 1, date["dayOfMonth"], date["hour"], date["minute"], date["second"]);
}

function getTimeUntil(date) {
    let milliseconds = date - Date.now();
    let days = Math.floor(milliseconds / (1000 * 60 * 60 * 24));
    milliseconds -= (days * 1000 * 60 * 60 * 24)
    let hours = Math.floor(milliseconds / (1000 * 60 * 60));
    milliseconds -= (hours * 1000 * 60 * 60)
    let minutes = Math.floor(milliseconds / (1000 * 60));
    milliseconds -= (minutes * 1000 * 60)
    let seconds = Math.floor(milliseconds / 1000);
    return {days, hours, minutes, seconds};
}

function clearAllCountdowns(article) {
    countdowns.forEach(countdown => clearInterval(countdown));
    countdowns = [];
}

function getCountdownTextContent(endIn, countdown) {
    if (countdown) {
        endIn.seconds --;
        if (endIn.seconds === -1) { endIn.seconds = 59; }
        endIn.seconds === 59 && endIn.minutes --;
        if (endIn.minutes === -1) { endIn.minutes = 59; }
        endIn.minutes === 59 && endIn.seconds === 59 && endIn.hours --;
        if (endIn.hours === -1) { endIn.hours = 23; }
        endIn.hours === 23 && endIn.minutes === 59 && endIn.seconds === 59 && endIn.days --;
        if (!endIn.seconds && !endIn.minutes && !endIn.hours && !endIn.days) { updateResults(); }
    }
    return (
        `L'enchère se termine dans: ` +
        `${!endIn.days ? "" : endIn.days + " jour(s), "}` +
        `${!endIn.hours ? "" : endIn.hours < 10 ? "0" + endIn.hours + " heure(s), ": endIn.hours + " heure(s), "}` +
        `${!endIn.minutes ? "" : endIn.minutes < 10 ? "0" + endIn.minutes + " minute(s)" : endIn.minutes + " minute(s)"}` +
        `${!endIn.seconds ? "" : endIn.seconds < 10 ? " et 0" + endIn.seconds + " seconde(s)": " et " + endIn.seconds + " seconde(s)"}.`
    );
}

function updateCheckboxes () {
    ["#saleIsOpen", "#isCurrentUser", "#saleIsWon"].forEach(enabledCheckbox => {
        let checkbox = document.querySelector(enabledCheckbox);
        checkbox.disabled = filter === "sell";
        checkbox.checked = false;
    });
    ["#saleIsOnGoing", "#saleIsCreated", "#saleIsOver"].forEach(disabledCheckbox => {
        let checkbox = document.querySelector(disabledCheckbox);
        checkbox.disabled = filter === "buy";
        checkbox.checked = false;
    });
}

function displayFilters() {
    document.querySelector("#filters").style.display = currentUser ? "block" : "none";
}

