// FUNCTIONS

async function getHighestBid(noArticle) {
    return await getData(`enchere/highest/${noArticle}`)
}

async function newBid(articleVendu, montantEnchere) {
    const data = {articleVendu, montantEnchere};
    return await addData("enchere/new", data)
}