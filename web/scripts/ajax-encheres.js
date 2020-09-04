// FUNCTIONS

async function getHighestBid(noArticle) {
    enchere = await getData(`enchere/highest/${noArticle}`)
    return enchere;
}

async function newBid(articleVendu, montantEnchere) {
    const data = {articleVendu, montantEnchere};
    return await addData("enchere/new", data)
}