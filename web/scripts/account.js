const createAccount = async function (pseudo, nom, prenom, email, telephone, rue, codePostal, motDePasse) {
    const data = {pseudo, nom, prenom, email, telephone, rue, codePostal, motDePasse};
    await addData(`utilisateur/signup`, data);
    await getSession();
}

const updateUser = async function (pseudo, nom, prenom, email, telephone, rue, codePostal, motDePasseActuel, motDePasse) {
    const data = {pseudo, nom, prenom, email, telephone, rue, codePostal, motDePasseActuel, motDePasse};
    await addData(`utilisateur/modify`, data);
    await getSession();
}

const authenticate = async function (pseudo, password, rememberMe) {
    await getData(`utilisateur/signin?pseudo=${pseudo}&motDePasse=${password}&rememberMe=${rememberMe}`);
    await getSession();
    location.reload();
}

const logout = async function() {
    await getData("utilisateur/signout")
    await getSession();
    location.reload();
}
