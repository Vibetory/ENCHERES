const getArticlesToBuy = async (userSearch = "", categorie = "", saleIsOpen = false, isCurrentUser = false, saleIsWon = false) => {
    const data = {userSearch, categorie, saleIsOpen, isCurrentUser, saleIsWon};
    let endpoint = "article/buy?"
    for (let [pointer, value] of Object.entries(data)) { endpoint += `${pointer}=${value}&`; }
    return await getData(endpoint);
}

const getArticlesToSell = async (userSearch = "", categorie = "", saleIsOnGoing = false, saleIsCreated = false, saleIsOver = false) => {
    const data = {userSearch, categorie, saleIsOnGoing, saleIsCreated, saleIsOver};
    let endpoint = "article/sell?"
    for (let [pointer, value] of Object.entries(data)) { endpoint += `${pointer}=${value}&`; }
    return await getData(endpoint);
}

