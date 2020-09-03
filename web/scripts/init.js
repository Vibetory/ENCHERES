// VARIABLES
let session,
    user,
    userSearch,
    categorie,
    filter,
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
    sources = [];
    component = {};


// FUNCTIONS

const getSession = async () => {
    session = await getData("utilisateur/session");
    return session;
}

const refresh = async () => {
    let delay = await getData("article/refresh");
    setTimeout(refresh, delay)
}

const init = async () => {
    await getSession();
    await refresh();
}



