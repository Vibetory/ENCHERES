// FUNCTIONS

function displayProfile(user) {
    ["pseudo", "nom", "prenom", "email", "telephone", "rue", "codePostal", "ville"].forEach(attribute => {
        document.querySelector(`#${attribute}`).innerHTML = user[attribute] ? user[attribute] : "Non renseigné";
    })
}


// INITIALIZATION:

function loadUsers() {
    displayProfile(user)
    if (user["noUtilisateur"] === session["noUtilisateur"]) {
        document.title = "Mon profil";
        let editButton = document.querySelector("#edit")
        editButton.className = "d-block";
        editButton.onclick = () => {
        }
    }
}

loadUsers()

