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
        let credits = document.querySelector("#credits");
        credits.innerHTML = session ? '<i class="fas fa-coins"> ' + session["credits"] : "";
        credits.classList.add(session ? "d-block" : "d-none");
        credits.classList.remove(session ? "d-none" : "d-block");
    });
}

function loadNavBar() {
    updateNavLinks().then();

    document.querySelector("#disconnect").onclick = () => logout();

    document.querySelector("#profile").onclick = async () => {
        user = session;
        await loadComponent("utilisateur");
    }

    document.querySelectorAll(".home").forEach(navLink => {
        navLink.onclick = () => {
            loadComponent("articles").then();
        }
    })

    document.querySelector("#signin-up").onclick = async () => {
        await loadComponent("connexion");
    }
}

loadNavBar();