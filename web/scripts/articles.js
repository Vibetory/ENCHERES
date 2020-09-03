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
    document.querySelector("#articles").innerHTML = "";
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
    container.style.border = "1px solid black";
    container.style.margin = "5px";
    container.style.width = "50%";
    container.style.padding = "5px";
    let title = document.createElement("h2");
    let description = document.createElement("h3");
    let prix = document.createElement("p");
    let dateEnd = getDate(article["dateFinEncheres"]);
    let dateStart = getDate(article["dateDebutEncheres"]);
    let notStarted = dateStart - Date.now() > 0;
    let finished = Date.now() - dateEnd > 0;
    let vendeur = document.createElement("p");
    title.textContent = article["nomArticle"];
    description.textContent = article["description"];
    prix.textContent = `Mise à prix: ${article["miseAPrix"]} point(s)`;
    let time;
    if (!finished) { time = getTimeUntil(notStarted ? dateStart: dateEnd); }
    let countdown = document.createElement("p");
    countdown.textContent = finished ? "L'enchère est terminée." : getCountdownTextContent(time, false, notStarted);
    countdown.style.color = finished ? "orange" : notStarted ? "blue" : "green";
    vendeur.textContent = `Vendeur: ${article["vendeur"]["pseudo"]}`;
    vendeur.onclick = async () => {
        let component = {url: "utilisateur.html", title: article["vendeur"]["pseudo"]};
        sources = ["users.js"];
        await loadComponent(component, sources)
            .then(() => {
            getUser(article["vendeur"]["pseudo"]).then();
        });
    }
    container.appendChild(title);
    container.appendChild(description);
    container.appendChild(prix);
    container.appendChild(countdown);
    container.appendChild(vendeur);
    if (!finished) {
        countdowns.push(
            setInterval(() => {
                countdown.textContent = getCountdownTextContent(time, true, notStarted);
            }, 1000)
        );
    }
    document.querySelector("#articles").appendChild(container);
}



function clearAllCountdowns(article) {
    countdowns.forEach(countdown => clearInterval(countdown));
    countdowns = [];
}

function getCountdownTextContent(time, countdown, notStarted) {
    if (countdown) {
        time.seconds --;
        if (time.seconds === -1) { time.seconds = 59; }
        time.seconds === 59 && time.minutes --;
        if (time.minutes === -1) { time.minutes = 59; }
        time.minutes === 59 && time.seconds === 59 && time.hours --;
        if (time.hours === -1) { time.hours = 23; }
        time.hours === 23 && time.minutes === 59 && time.seconds === 59 && time.days --;
        if (!time.seconds && !time.minutes && !time.hours && !time.days) { updateResults(); }
    }
    return (
        `L'enchère ${notStarted ? "commence" : "se termine"} dans: ` +
        `${!time.days ? "" : time.days + " jour(s), "}` +
        `${!time.hours ? "" : time.hours < 10 ? "0" + time.hours + " heure(s), ": time.hours + " heure(s), "}` +
        `${!time.minutes ? "" : time.minutes < 10 ? "0" + time.minutes + " minute(s)" : time.minutes + " minute(s)"}` +
        `${!time.seconds ? "" : time.seconds < 10 ? " et 0" + time.seconds + " seconde(s)": " et " + time.seconds + " seconde(s)"}.`
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
    getSession().then(session => {
        document.querySelector("#filters").style.display = session ? "block" : "none";
    })
}



// INITIALIZATION

function loadArticles() {
    console.log("loadArticles");
    userSearch = document.querySelector("#userSearch").value;
    categorie = document.querySelector("#categorie").value;
    filter = document.querySelector('input[name="filter"]:checked').value;
    listArticles = [];
    countdowns = [];
    checkboxes = {
        saleIsOpen: false,
        isCurrentUser: false,
        saleIsWon: false,
        saleIsOnGoing: false,
        saleIsCreated: false,
        saleIsOver: false
    };

    updateResults();
    displayFilters();
    updateCheckboxes();
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
}

loadArticles();

