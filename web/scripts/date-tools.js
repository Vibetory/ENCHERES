// FUNCTIONS

function getDate(date) {
    return new Date(date["year"], date["monthValue"] - 1, date["dayOfMonth"], date["hour"], date["minute"], date["second"]);
}

function getTimeUntil(date) {
    let milliseconds = date - Date.now();
    let days = Math.floor(milliseconds / (1000 * 60 * 60 * 24));
    milliseconds -= (days * 1000 * 60 * 60 * 24)
    let hours = Math.floor(milliseconds / (1000 * 60 * 60));
    milliseconds -= (hours * 1000 * 60 * 60)
    let minutes = Math.floor(milliseconds / (1000 * 60));
    milliseconds -= (minutes * 1000 * 60)
    let seconds = Math.floor(milliseconds / 1000);
    return {days, hours, minutes, seconds};
}