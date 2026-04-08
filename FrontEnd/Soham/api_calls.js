function setupLoginForm() {
  try {
    const form = document.querySelector(".logdiv form");
    if (!form) return;
    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      const emailEl = document.getElementById("exampleInputEmail1");
      const passEl = document.getElementById("exampleInputPassword1");
      const email = emailEl ? emailEl.value.trim() : "";
      const password = passEl ? passEl.value : "";
      if (!email || !password) {
        window.alert("Email and password required");
        return;
      }
      console.log(JSON.stringify({ email: email, password: password }));
      try {
        const res = await fetch("http://localhost:8080/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email: email, password: password }),
        });
        if (res.status === 200) {
          localStorage.setItem("userEmail", email);
          window.location.href = "home.html";
        } else if (res.status === 401) {
          window.alert("Invalid credentials");
        } else {
          window.alert("Login failed: " + res.status);
        }
      } catch (err) {
        console.error(err);
        window.alert("Network error");
      }
    });
  } catch (e) {
    console.error("setupLoginForm error", e);
  }
}

async function fetchUserTrips() {
  const email = localStorage.getItem("userEmail");
  if (!email) return;

  const path = window.location.pathname.split("/").pop();
  if (path !== "home.html") return;

  const grid = document.querySelector(".trip-grid");
  if (grid) {
    grid.innerHTML = ""; // Clear hardcoded trips immediately
  }

  try {
    const res = await fetch("http://localhost:8080/gettripsbyemail", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ creatorEmail: email }),
    });

    if (res.status === 200) {
      const text = await res.text();
      let data;
      try {
        data = JSON.parse(text);
      } catch (e) {
        console.error("Failed to parse JSON response:", text);
        return;
      }
      if (data && data.tripIDs) {
        localStorage.setItem("tripIDs", JSON.stringify(data.tripIDs));
        console.log("Stored tripIDs in localStorage:", data.tripIDs);
        await renderTrips(data.tripIDs);
      }
    } else {
      console.error("Failed to fetch trips. Status:", res.status);
    }
  } catch (err) {
    console.error("Network error fetching trips:", err);
  }
}

async function renderTrips(tripIDs) {
  const grid = document.querySelector(".trip-grid");
  if (!grid || !tripIDs || tripIDs.length === 0) return;

  const images = [
    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1400&q=80",
    "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=1400&q=80",
    "https://images.unsplash.com/photo-1467269204594-9661b134dd2b?auto=format&fit=crop&w=1400&q=80",
    "https://images.unsplash.com/photo-1433838552652-f9a46b332c40?auto=format&fit=crop&w=1400&q=80",
    "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1400&q=80",
    "https://images.unsplash.com/photo-1527631746610-bca00a040d60?auto=format&fit=crop&w=1400&q=80",
  ];

  for (const id of tripIDs) {
    try {
      const res = await fetch("http://localhost:8080/gettripbyid", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ tripID: id.toString() }),
      });

      if (res.status === 200) {
        const text = await res.text();
        const trip = JSON.parse(text);

        const imgUrl = images[Math.floor(Math.random() * images.length)];

        const article = document.createElement("article");
        article.className = "trip-card";

        article.innerHTML = `
          <img src="${imgUrl}" alt="${trip.destination}" />
          <div class="card-copy">
            <h3>${trip.destination}</h3>
            <p>${trip.startDate} to ${trip.endDate} · ${trip.budget} INR</p>
            <br />
            <button type="button" class="btn btn-primary" value="${id}">
              Details
            </button>
          </div>
        `;

        grid.appendChild(article);
      }
    } catch (err) {
      console.error(`Failed to load trip ${id}:`, err);
    }
  }

  // Re-run the visual reveal observer on the new cards
  if (typeof setupCardReveal === "function") {
    setupCardReveal();
  }
}

function setupSignupForm() {
  try {
    const path = window.location.pathname.split("/").pop();
    if (path !== "signup.html") return;

    const form = document.querySelector(".logdiv form");
    if (!form) return;

    form.addEventListener("submit", async (e) => {
      e.preventDefault();

      const first_name = document.getElementById("firstName")?.value.trim();
      const last_name = document.getElementById("lastName")?.value.trim();
      const email = document.getElementById("emailInput")?.value.trim();
      const password = document.getElementById("passwordInput")?.value;
      const dob = document.getElementById("dobInput")?.value;

      if (!email || !password || !first_name || !last_name || !dob) {
        window.alert("All fields are required");
        return;
      }

      const payload = { email, password, first_name, last_name, dob };
      console.log("Signup Payload:", JSON.stringify(payload));

      try {
        const res = await fetch("http://localhost:8080/signup", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });

        if (res.status === 200 || res.status === 201) {
          localStorage.setItem("userEmail", email);
          window.location.href = "home.html";
        } else if (res.status === 400 || res.status === 409) {
          const errMsg = await res.text();
          window.alert("Signup failed: " + errMsg);
        } else {
          window.alert("Signup failed with status: " + res.status);
        }
      } catch (err) {
        console.error(err);
        window.alert("Network error");
      }
    });
  } catch (e) {
    console.error("setupSignupForm error", e);
  }
}

function setupCreateTripUI() {
  const path = window.location.pathname.split("/").pop();
  if (path !== "createTrip.html") return;

  const addDayBtn = document.getElementById("addDayBtn");
  const daysContainer = document.getElementById("daysContainer");
  const saveTripBtn = document.getElementById("saveTripBtn");

  if (!addDayBtn || !daysContainer || !saveTripBtn) return;

  // Track state
  let currentTripID = null;
  let isTripSaved = false;
  addDayBtn.disabled = true;

  // Trip save logic
  saveTripBtn.onclick = async () => {
    const dest = document.getElementById("destination")?.value.trim();
    const budg = document.getElementById("budget")?.value;
    const start = document.getElementById("startDate")?.value;
    const end = document.getElementById("endDate")?.value;
    const email = localStorage.getItem("userEmail");

    if (!dest || !budg || !start || !end || !email) {
      window.alert("Please fill all trip details");
      return;
    }

    const payload = {
      creatorEmail: email,
      destination: dest,
      budget: budg.toString(),
      startDate: start,
      endDate: end,
    };

    try {
      const res = await fetch("http://localhost:8080/createtrip", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (res.status === 201) {
        const text = await res.text();
        currentTripID = text.trim();
        isTripSaved = true;
        saveTripBtn.disabled = true;
        saveTripBtn.textContent = "Trip Saved ✓";
        addDayBtn.disabled = false;
        daysContainer.querySelectorAll(".save-day-btn").forEach((btn) => {
          btn.disabled = false;
        });
      } else {
        window.alert("Failed to save trip overview");
      }
    } catch (err) {
      console.error(err);
      window.alert("Server error");
    }
  };

  // Function to create a new event element
  const createEventEl = (dayIdx, eventIdx, isDaySavedArg, dayID) => {
    const div = document.createElement("div");
    div.className =
      "event-item bg-white border rounded-3 p-3 mb-2 position-relative shadow-xs";
    div.dataset.eventIndex = eventIdx;
    div.innerHTML = `
      <div class="row g-2">
        <div class="col-md-2">
          <label class="form-label small">Time</label>
          <input type="time" class="form-control form-control-sm event-time" required />
        </div>
        <div class="col-md-3">
          <label class="form-label small">Type</label>
          <select class="form-select form-select-sm event-type" required>
            <option value="Transport">Transport</option>
            <option value="Accommodation">Accommodation</option>
            <option value="Place">Tourist Destination</option>
            <option value="Food">Food Stop</option>
            <option value="Other">Other</option>
          </select>
        </div>
        <div class="col-md-5">
          <label class="form-label small">Description</label>
          <input type="text" class="form-control form-control-sm event-desc" placeholder="Details..." required />
        </div>
        <div class="col-md-2 d-flex align-items-end gap-1">
          <button class="btn btn-sm btn-outline-success save-event-btn" type="button" ${!isDaySavedArg ? "disabled" : ""}>Save</button>
          <button class="btn btn-sm btn-outline-danger remove-event-btn" type="button">×</button>
        </div>
      </div>
    `;

    const saveBtn = div.querySelector(".save-event-btn");
    saveBtn.onclick = async () => {
      const timeVal = div.querySelector(".event-time")?.value;
      const typeVal = div.querySelector(".event-type")?.value;
      const descVal = div.querySelector(".event-desc")?.value.trim();

      if (!timeVal || !typeVal || !descVal) {
        window.alert("Fill event data");
        return;
      }

      const payload = {
        dayID: dayID.toString(),
        tripID: currentTripID.toString(),
        time: timeVal + ":00",
        type: typeVal,
        description: descVal,
        link: "",
      };

      try {
        const res = await fetch("http://localhost:8080/createevent", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        if (res.status === 201) {
          saveBtn.disabled = true;
          saveBtn.textContent = "Saved";
          div.classList.add("border-success");
        }
      } catch (err) {
        console.error(err);
      }
    };

    div.querySelector(".remove-event-btn").onclick = () => div.remove();
    return div;
  };

  // Function to create a new day block
  const createDayEl = (idx) => {
    const div = document.createElement("div");
    div.className = "day-block border rounded-3 p-3 mb-4 bg-light shadow-sm";
    div.dataset.dayIndex = idx;
    div.innerHTML = `
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="mb-0">Day ${idx + 1}</h5>
        <div class="d-flex gap-2">
          <input type="date" class="form-control form-control-sm day-date" style="max-width: 150px;" required />
          <button type="button" class="btn btn-sm btn-success save-day-btn" ${!isTripSaved ? "disabled" : ""}>Save Day</button>
          <button type="button" class="btn btn-sm btn-outline-danger remove-day-btn">Remove</button>
        </div>
      </div>
      <div class="events-list ms-md-4 mb-2"></div>
      <button type="button" class="btn btn-link btn-sm text-decoration-none add-event-btn" disabled>+ Add Event</button>
    `;

    const eventsList = div.querySelector(".events-list");
    const addEvBtn = div.querySelector(".add-event-btn");
    const saveDayBtn = div.querySelector(".save-day-btn");
    let savedDayID = null;

    saveDayBtn.onclick = async () => {
      const dDate = div.querySelector(".day-date")?.value;
      if (!dDate) return window.alert("Select date");

      try {
        const res = await fetch("http://localhost:8080/createday", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ tripID: currentTripID, date: dDate }),
        });

        if (res.status === 201) {
          savedDayID = (await res.text()).trim();
          saveDayBtn.disabled = true;
          saveDayBtn.textContent = "Day Saved ✓";
          addEvBtn.disabled = false;
          // Refresh event placeholder with ID
          eventsList.innerHTML = "";
          eventsList.appendChild(createEventEl(idx, 0, true, savedDayID));
        }
      } catch (err) {
        console.error(err);
      }
    };

    eventsList.appendChild(createEventEl(idx, 0, false, null));

    addEvBtn.onclick = () => {
      eventsList.appendChild(
        createEventEl(idx, eventsList.children.length, true, savedDayID),
      );
    };

    div.querySelector(".remove-day-btn").onclick = () => {
      div.remove();
      Array.from(daysContainer.children).forEach((d, i) => {
        d.querySelector("h5").textContent = `Day ${i + 1}`;
      });
    };

    return div;
  };

  addDayBtn.onclick = () => {
    daysContainer.appendChild(createDayEl(daysContainer.children.length));
  };

  daysContainer.innerHTML = "";
  daysContainer.appendChild(createDayEl(0));
}
