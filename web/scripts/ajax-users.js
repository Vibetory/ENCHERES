async function createAccount(pseudo, nom, prenom, email, telephone, rue, codePostal, motDePasse) {
    const data = {pseudo, nom, prenom, email, telephone, rue, codePostal, motDePasse};
    await addData(`utilisateur/signup`, data);
    await getSession();
}

async function updateUser(pseudo, nom, prenom, email, telephone, rue, codePostal, motDePasseActuel, motDePasse) {
    const data = {pseudo, nom, prenom, email, telephone, rue, codePostal, motDePasseActuel, motDePasse};
    await addData(`utilisateur/modify`, data);
    await getSession();
}

// FUNCTIONS

async function authenticate(pseudo, password, rememberMe) {
    await getData(`utilisateur/signin?pseudo=${pseudo}&motDePasse=${password}&rememberMe=${rememberMe}`);
    await getSession();
    location.reload();
}

async function logout() {
    await getData("utilisateur/signout")
    await getSession();
    location.reload(true);
}

async function deleteAccount() {
    await getData("utilisateur/delete/" + user["noUtilisateur"]);
    await logout();
}

async function getUser(pseudo) {
    user = await getData("utilisateur/" + pseudo)
    return user;
}