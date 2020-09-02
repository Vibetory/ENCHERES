let currentUser;
let refreshDelay;

const getSession = async () => {
    currentUser = await getData("utilisateur/session");
}

const refresh = async () => {
    refreshDelay = await getData("article/refresh");
    setTimeout(refresh, refreshDelay)
}

const init = async () => {
    await getSession();
    await refresh();
}

