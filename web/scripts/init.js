// VARIABLES
let session,
    user,
    userSearch,
    categorie,
    filter,
    enchere,
    checkboxes = {
        saleIsOpen: false,
        isCurrentUser: false,
        saleIsWon: false,
        saleIsOnGoing: false,
        saleIsCreated: false,
        saleIsOver: false
    },
    listArticles = [],
    countdowns = [];


// FUNCTIONS

const getSession = async () => {
    session = await getData("utilisateur/session");
    return session;
}

const refresh = async () => {
    let delay = await getData("article/refresh");
    console.log(`The database will automatically refresh in ${delay / 1000} seconds.`)
    if (!delay) { setTimeout(refresh, delay); }

}

const init = async () => {
    await getSession();
    await refresh();
}



