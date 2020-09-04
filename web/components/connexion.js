// INITIALIZATION:

function loadConnexion() {
    document.querySelector("#connect").onclick = async $event => {
        $event.preventDefault();
        let pseudo = document.querySelector("#pseudo").value;
        let motDePasse = document.querySelector("#motDePasse").value;
        let rememberMe = document.querySelector("#rememberMe").checked;
        await authenticate(pseudo, motDePasse, rememberMe).then(json => {
            if (!json["noUtilisateur"]) {
                let message = document.querySelector("#message");
                message.textContent = json["message"];
                message.classList.remove("d-none");
            } else { location.reload(); }
        })
    }
}

loadConnexion()