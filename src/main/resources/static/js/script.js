async function load() {
  const statusEl = document.getElementById("status");

  try {
    const res = await fetch("/api/v1/measurements/current");

    if (!res.ok) {
      statusEl.innerText = "OFFLINE";
      statusEl.style.color = "red";
      return;
    }

    const data = await res.json();

    if (!data) {
      statusEl.innerText = "NO DATA";
      statusEl.style.color = "orange";
      return;
    }

    document.getElementById("temp").innerText = data.temperature ?? "--";
    document.getElementById("hum").innerText = data.humidity ?? "--";

    statusEl.innerText = "ONLINE";
    statusEl.style.color = "lime";
  } catch (e) {
    statusEl.innerText = "OFFLINE";
    statusEl.style.color = "red";
  }
}

setInterval(load, 3000);
load();
