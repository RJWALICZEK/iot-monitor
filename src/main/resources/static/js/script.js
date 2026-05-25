async function load() {
    const res = await fetch("/api/v1/measurements/current");
    const data = await res.json();

    document.getElementById("temp").innerText = data.temperature;
    document.getElementById("hum").innerText = data.humidity;
}

setInterval(load, 3000);
load();