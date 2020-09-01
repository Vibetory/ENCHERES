const authenticate = async function (pseudo, password, rememberMe) {
    await getData(`utilisateur/signin?pseudo=${pseudo}&motDePasse=${password}&rememberMe=${rememberMe}`);
    await getSession();
}

const logout = async function() {
    await getData("utilisateur/signout")
    await getSession();
}
