// FUNCTIONS

async function getArticlesToBuy(userSearch = "", categorie = "", saleIsOpen = false, isCurrentUser = false, saleIsWon = false){
    const data = {userSearch, categorie, saleIsOpen, isCurrentUser, saleIsWon};
    let endpoint = "article/buy?"
    for (let [pointer, value] of Object.entries(data)) { endpoint += `${pointer}=${value}&`; }
    return await getData(endpoint);
}

async function getArticlesToSell(userSearch = "", categorie = "", saleIsOnGoing = false, saleIsCreated = false, saleIsOver = false) {
    const data = {userSearch, categorie, saleIsOnGoing, saleIsCreated, saleIsOver};
    let endpoint = "article/sell?"
    for (let [pointer, value] of Object.entries(data)) { endpoint += `${pointer}=${value}&`; }
    return await getData(endpoint);
}

async function newArticle(nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, categorie, rue = "", codePostal = "", ville = "") {
    const data = {nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, categorie, rue, codePostal, ville}
    return addData("article/new", data);
}

