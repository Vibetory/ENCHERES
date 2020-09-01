const authenticate = async function (pseudo, password) {
    await getData(`utilisateur/signin?pseudo=${pseudo}&motDePasse=${password}`);
    await getSession();
}

const logout = async function() {
    await getData("utilisateur/signout")
    await getSession();
}
