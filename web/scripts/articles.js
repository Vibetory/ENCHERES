const getArticlesToBuy = async (userSearch = "", categorie = "", saleIsOpen = false, isCurrentUser = false, saleIsWon = false) => {
    const data = {userSearch, categorie, saleIsOpen, isCurrentUser, saleIsWon};
    let endpoint = "article/buy?"
    for (let [key, value] of Object.entries(data)) { endpoint += `${key}=${value}&`; }
    return await getData(endpoint);
}

const getArticlesToSell = async (userSearch = "", categorie = "", saleIsOpen = false, saleIsCreated = false, saleIsOver = false) => {
    const data = {userSearch, categorie, saleIsOpen, saleIsCreated, saleIsOver};
    let endpoint = "article/sell?"
    for (let [key, value] of Object.entries(data)) { endpoint += `${key}=${value}&`; }
    return await getData(endpoint);
}