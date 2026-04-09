async function injectSection(filePath, targetId, selector) {
  try {
    const response = await fetch(filePath);
    if (!response.ok) {
      throw new Error(`Failed to load ${filePath}`);
    }

    const html = await response.text();
    const parser = new DOMParser();
    const parsed = parser.parseFromString(html, "text/html");
    const sourceNode = parsed.querySelector(selector);
    const targetNode = document.getElementById(targetId);

    if (sourceNode && targetNode) {
      targetNode.innerHTML = sourceNode.outerHTML;
    }
  } catch (error) {
    console.error(error);
  }
}

function setupCardReveal() {
  const cards = document.querySelectorAll(".trip-card");

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("is-visible");
          observer.unobserve(entry.target);
        }
      });
    },
    { threshold: 0.25 },
  );

  cards.forEach((card) => observer.observe(card));
}

function setupCreateTripButton() {
  const createBtn = document.getElementById("createTripBtn");
  if (!createBtn) {
    return;
  }

  createBtn.addEventListener("click", () => {
    window.alert(
      "Your custom trip planner is ready. Start building your itinerary!",
    );
  });
}

document.addEventListener("DOMContentLoaded", async () => {
  await Promise.all([
    injectSection("navbar.html", "navbar-slot", "nav"),
    injectSection("footer.html", "footer-slot", ".footer"),
  ]);

  updateNavbarButton();
  // Redirect to index.html if user not authenticated (unless on public pages)
  enforceAuthRedirect();

  setupCardReveal();
  setupCreateTripButton();
  setupBackgroundMusic();
  setupLoginForm();

  if (typeof setupSignupForm === "function") {
    setupSignupForm();
  }

  if (typeof fetchUserTrips === "function") {
    fetchUserTrips();
  }

  if (typeof setupCreateTripUI === "function") {
    setupCreateTripUI();
  }

  if (typeof setupViewTripUI === "function") {
    setupViewTripUI();
  }

  if (typeof setupProfileUI === "function") {
    setupProfileUI();
  }
});

function updateNavbarButton() {
  try {
    const anchor = document.getElementById("navbar-btn");
    if (!anchor) return;
    const email = localStorage.getItem("userEmail");
    const id = localStorage.getItem("userID");
    const btn = anchor.querySelector("button");
    if (email || id) {
      anchor.setAttribute("href", "profile.html");
      if (btn) btn.textContent = "Your Profile";
    } else {
      anchor.setAttribute("href", "login.html");
      if (btn) btn.textContent = "Login";
    }
  } catch (e) {
    console.error("updateNavbarButton error:", e);
  }
}

function enforceAuthRedirect() {
  try {
    const email = localStorage.getItem("userEmail");
    const id = localStorage.getItem("userID");
    if (email || id) return; // authenticated

    // allow public pages
    const path = window.location.pathname.split("/").pop();
    const publicPages = ["", "index.html", "login.html", "signup.html"];
    if (publicPages.includes(path)) return;

    // otherwise redirect to index
    window.location.href = "index.html";
  } catch (e) {
    console.error("enforceAuthRedirect error:", e);
  }
}

function setupBackgroundMusic() {
  const audio = document.getElementById("bg-music");
  const btn = document.getElementById("musicToggle");
  if (!audio || !btn) return;

  audio.volume = 0.5;

  const playAudio = async () => {
    try {
      await audio.play();
      btn.classList.add("on");
      btn.textContent = "♪";
    } catch (e) {
      // autoplay blocked; keep button in play state indicator
      btn.classList.remove("on");
      btn.textContent = "▷";
    }
  };

  const pauseAudio = () => {
    audio.pause();
    btn.classList.remove("on");
    btn.textContent = "▷";
  };

  // Attempt to start playback on load (user requested default ON).
  // Browsers may block autoplay — if blocked, the catch sets the button to '▷'.
  playAudio();

  btn.addEventListener("click", (e) => {
    e.stopPropagation();
    if (audio.paused) playAudio();
    else pauseAudio();
  });
}
