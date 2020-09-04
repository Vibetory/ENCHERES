// FUNCTIONS

async function loadComponent (component) {
    document.querySelector("#root").innerHTML = "";
    document.querySelector("#scripts").innerHTML = "";
    let html = `components/${component}.html`;
    let script = `components/${component}.js`;
    fetch(html)
        .then(data => data.text())
        .then(html => document.querySelector("#root").innerHTML = html)
        .then(() => { loadScript(script); });
}

function loadScript(source) {
    const script = document.createElement("script");
    script.src = source;
    script.type = "text/javascript";
    document.querySelector("#scripts").appendChild(script);
}