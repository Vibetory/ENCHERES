// FUNCTIONS

function displayProfile(user) {
    ["pseudo", "nom", "prenom", "email", "telephone", "rue", "codePostal", "ville"].forEach(attribute => {
        document.querySelector(`#${attribute}`).innerHTML = user[attribute] ? user[attribute] : "Non renseigné";
    })
}


// INITIALIZATION:

function loadUsers() {
    console.log("loadUsers");
    displayProfile(user)
    document.querySelector("#home").onclick = () => {
        let component = {url: "articles.html", title: "Liste des enchères"};
        let sources = ["ajax-users.js", "ajax-articles.js", "ajax-encheres.js", "date-tools.js", "articles.js"];
        loadComponent(component, sources).then();
    }
}

loadUsers()

