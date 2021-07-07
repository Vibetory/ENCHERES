// FUNCTIONS

async function displayArticle(enchere) {
    let article = enchere["articleVendu"] ? enchere["articleVendu"] : enchere;
    const vendeur = article["vendeur"];
    ["nomArticle", "description", "categorie", "highestBid", "miseAPrix"].forEach(attribute => {
        document.querySelector(`#${attribute}`).innerHTML =
            article[attribute] ?
                article[attribute] :
                article[attribute] === 0 ?
                    0 : "Aucune";
    });
    document.querySelector(`#${"dateFinEncheres"}`).innerHTML = getDate(article["dateFinEncheres"]);
    let pseudo = document.querySelector("#vendeur");
    pseudo.innerHTML = vendeur["pseudo"];
    pseudo.onclick = async () => {
        await loadComponent("utilisateur")
            .then(() => { getUser(vendeur["pseudo"]).then(); });
    }
    await getRetrait(article["noArticle"]).then(retrait => {
        ["rue", "codePostal", "ville"].forEach(attribute => {
            document.querySelector(`#${attribute}`).innerHTML = retrait[attribute] ? retrait[attribute] : "Non renseigné";
        })
    })
    await getSession().then(session => {
        if (session && session["noUtilisateur"] !== article["vendeur"]["noUtilisateur"]) {
            let minimum = enchere["montantEnchere"] ? enchere["montantEnchere"] + 1 : article["miseAPrix"] + 1;
            let proposition = document.querySelector("#proposition");
            let montant = document.querySelector("#montantEnchere");
            proposition.classList.add("d-block");
            proposition.classList.remove("d-none");
            montant.value = minimum;
            montant.min = minimum;
            montant.max = session["credits"];
        }
    })

}


// INITIALIZATION:

function loadArticle() {
    displayArticle(enchere).then();
}

loadArticle()
