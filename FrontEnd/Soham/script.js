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

  setupCardReveal();
  setupCreateTripButton();
  setupBackgroundMusic();
});

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
