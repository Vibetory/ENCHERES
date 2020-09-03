// FUNCTIONS

async function loadComponent (component, sources = []) {
    document.querySelector("#root").innerHTML = "";
    document.querySelector("#scripts").innerHTML = "";
    fetch(`components/${component.url}`)
        .then(data => data.text())
        .then(html => document.querySelector("#root").innerHTML = html)
        .then(() => {
            sources.forEach(source => {
                loadScript(`scripts/${source}`);
            })
            document.title = component.title;
        });
}

function loadScript(source) {
    const script = document.createElement("script");
    script.src = source;
    script.type = "text/javascript";
    document.querySelector("#scripts").appendChild(script);
}