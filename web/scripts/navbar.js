async function updateNavLinks() {
    await getSession().then(session => {
        document.querySelectorAll(".logged-in").forEach(navLink => {
            navLink.classList.add(session ? "d-block" : "d-none");
            navLink.classList.remove(session ? "d-none" : "d-block");
        });
        document.querySelectorAll(".logged-out").forEach(navLink => {
            navLink.classList.add(session ? "d-none" : "d-block");
            navLink.classList.remove(session ? "d-block" : "d-none");
        });
    });
}

function loadNavBar() {
    updateNavLinks().then();

    document.querySelector("#disconnect").onclick = () => logout();

    document.querySelector("#profile").onclick = async () => {
        user = session;
        component = {url: "utilisateur.html", title: "Mon profil"};
        sources = ["utilisateur.js"];
        await loadComponent(component, sources);
    }

    document.querySelectorAll(".home").forEach(navLink => {
        navLink.onclick = () => {
            component = {url: "articles.html", title: "Liste des enchères"};
            sources = ["ajax-utilisateur.js", "ajax-articles.js", "ajax-encheres.js", "date-tools.js", "articles.js"];
            loadComponent(component, sources).then();
        }
    })

    document.querySelector("#signin-up").onclick = async () => {
        component = {url: "connexion.html", title: "S'inscrire / Se connecter"};
        sources = ["connexion.js"];
        await loadComponent(component, sources);
    }
}

loadNavBar();