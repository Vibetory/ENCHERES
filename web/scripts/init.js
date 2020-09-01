let currentUser;
let refreshDelay;

const getSession = async function() {
    currentUser = await getData("utilisateur/session");
}

const refresh = async function() {
    refreshDelay = await getData("article/refresh");
    setTimeout(refresh, refreshDelay)
}

const init = async function() {
    await getSession();
    await refresh();
}

init();


