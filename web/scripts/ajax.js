const ajax = async (method, endpoint = "", data = "") => {
    const parameters = { method: method, headers: {'Accept': 'application/json'} }
    if (data) {
        parameters.headers['Content-type'] = 'application/json';
        parameters.body = JSON.stringify(data);
    }
    const response = await fetch("http://localhost:8080/api/" + endpoint, parameters);
    return await response.json();
}

const getData = async (endpoint, data = "") => { return await ajax("GET", endpoint || data["endpoint"], data); }

const deleteData = async (endpoint) => { await ajax("DELETE", endpoint); }

const addData = async (data) => { await ajax("POST", data["endpoint"], data); }

const updateData = async (data) => { await ajax("PUT", data["endpoint"], data); }
