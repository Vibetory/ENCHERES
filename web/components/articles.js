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
    document.querySelector("#articles").innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
    search().then(articles => {
            listArticles = articles ? articles : [];
            document.querySelector("#articles").innerHTML = "";
            listArticles.forEach(article => {
                createArticle(article)
            })
        });
}

function createArticle(article) {
    let card = document.createElement("div");
    card.classList.add("card", "mx-auto", "border-dark", "col-12", 'col-sm-6', "col-lg-4", "col-xl-3");
    card.style.width = "18rem";
    let image = document.createElement("img");
    image.src = "https://us.123rf.com/450wm/pavelstasevich/pavelstasevich1811/pavelstasevich181101028/112815904-stock-vector-no-image-available-icon-flat-vector-illustration.jpg?ver=6";
    image.width = 300;
    image.className = "card-img-top";
    card.appendChild(image);
    let header = document.createElement("div");
    header.className = "card-body";
    let title = setUpTitle(article);
    header.appendChild(title);
    let description = document.createElement("p");
    description.className = "card-text";
    description.textContent = article["description"];
    header.appendChild(description);
    card.appendChild(header);
    let details = document.createElement("ul");
    details.classList.add("list-group", "list-group-flush");
    let prix = document.createElement("li");
    prix.className = "list-group-item";
    prix.textContent = `Mise à prix: ${article["miseAPrix"]} point(s)`;
    details.appendChild(prix);
    let countdown = setUpCountdown(article);
    details.appendChild(countdown);
    let vendeur = setUpVendeur(article);
    details.appendChild(vendeur);
    card.appendChild(details);
    document.querySelector("#articles").appendChild(card);
}

function setUpTitle(article) {
    let title = document.createElement("h5"); // Add link and loadComponent();
    title.className = "card-title";
    title.textContent = article["nomArticle"];
    title.onclick = async () => {
        await getHighestBid(article["noArticle"]).then(() => { if (!enchere) {enchere = article; }})
            .then(async() => { await loadComponent("enchere"); });
    }
    return title;
}

function setUpCountdown(article) {
    let time;
    let dateStart = getDate(article["dateDebutEncheres"]);
    let dateEnd = getDate(article["dateFinEncheres"]);
    let isNotStarted = dateStart - Date.now() > 0;
    let isOver = Date.now() - dateEnd > 0;
    if (!isOver) { time = getTimeUntil(isNotStarted ? dateStart: dateEnd); }
    let countdown = document.createElement("li");
    countdown.className = "list-group-item";
    countdown.textContent = isOver ? "L'enchère est terminée." : getCountdownTextContent(time, false, isNotStarted);
    if (!isOver) {
        countdowns.push(
            setInterval(() => {
                countdown.textContent = getCountdownTextContent(time, true, isNotStarted);
            }, 1000)
        );
    }
    return countdown;
}

function setUpVendeur(article) {
    let vendeur = document.createElement("li");
    vendeur.className = "list-group-item";
    let pseudoVendeur = article["vendeur"]["pseudo"];
    vendeur.textContent = `Vendeur: ${pseudoVendeur}`;
    vendeur.onclick = async () => {
        await loadComponent("utilisateur")
            .then(() => { getUser(pseudoVendeur).then(); });
    }
    return vendeur;
}

function clearAllCountdowns(article) {
    countdowns.forEach(countdown => clearInterval(countdown));
    countdowns = [];
}

function getCountdownTextContent(time, countdown, isNotStarted) {
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
        `${isNotStarted ? "Démarre" : "Fin"} dans ` +
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
        document.querySelector("#filters").className = session ? "d-block" : "d-none";
    })
}



// INITIALIZATION

function loadArticles() {
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

