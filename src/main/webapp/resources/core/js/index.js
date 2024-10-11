const monthNames = [
  "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
  "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
];

const daysOfWeek = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"];

const daysInMonth = (month, year) => new Date(year, month + 1, 0).getDate();
const firstDayOfMonth = (month, year) => new Date(year, month, 1).getDay();

let currentMonth = new Date().getMonth();
let currentYear = new Date().getFullYear();

const monthNameEl = document.getElementById("monthName");
const daysOfWeekEl = document.getElementById("daysOfWeek");
const daysContainer = document.getElementById("daysContainer");
const prevButton = document.getElementById("prev");
const nextButton = document.getElementById("next");

// Rellenar los días de la semana
daysOfWeek.forEach(day => {
  const dayOfWeekEl = document.createElement("div");
  dayOfWeekEl.textContent = day;
  dayOfWeekEl.classList.add("day-of-week");
  daysOfWeekEl.appendChild(dayOfWeekEl);
});

const renderCalendar = () => {
  monthNameEl.textContent = `${monthNames[currentMonth]} ${currentYear}`;
  daysContainer.innerHTML = "";

  const days = daysInMonth(currentMonth, currentYear);
  const firstDay = firstDayOfMonth(currentMonth, currentYear);

  // Añadir espacios en blanco para los días anteriores al primer día del mes
  for (let i = 0; i < firstDay; i++) {
    const emptyDay = document.createElement("div");
    daysContainer.appendChild(emptyDay);
  }

  // Añadir los días del mes
  for (let i = 1; i <= days; i++) {
    const dayEl = document.createElement("div");
    dayEl.textContent = i;
    dayEl.classList.add("day");
    daysContainer.appendChild(dayEl);
  }

  // Deshabilitar botones si se llega al límite de meses
  prevButton.disabled = currentMonth === 0 && currentYear === new Date().getFullYear() - 10;
  nextButton.disabled = currentMonth === 11 && currentYear === new Date().getFullYear() + 10;
};

// Manejador para el botón "Anterior"
prevButton.addEventListener("click", () => {
  if (currentMonth === 0) {
    currentMonth = 11;
    currentYear--;
  } else {
    currentMonth--;
  }
  renderCalendar();
});

// Manejador para el botón "Siguiente"
nextButton.addEventListener("click", () => {
  if (currentMonth === 11) {
    currentMonth = 0;
    currentYear++;
  } else {
    currentMonth++;
  }
  renderCalendar();
});

// Renderizar el calendario inicial
renderCalendar();
