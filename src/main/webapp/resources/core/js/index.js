const monthNames = [
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
];

const daysOfWeek = ["Dom", "Lun", "Mar", "Mier", "Jue", "Vie", "Sab"];

const daysInMonth = (month, year) => new Date(year, month + 1, 0).getDate();
const firstDayOfMonth = (month, year) => new Date(year, month, 1).getDay(); // Corregido para obtener el primer día correctamente

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

// Función para convertir la fecha correctamente sin problemas de zona horaria
const parseFechaSinOffset = fecha => {
    const [year, month, day] = fecha.split('-');
    return new Date(Date.UTC(year, month - 1, day)); // Usamos UTC para evitar desfases
};

const renderCalendar = () => {
    monthNameEl.textContent = `${monthNames[currentMonth]} ${currentYear}`;
    daysContainer.innerHTML = "";

    const days = daysInMonth(currentMonth, currentYear);
    const firstDay = firstDayOfMonth(currentMonth, currentYear);

    // Obtener las fechas desde sessionStorage
    const fechasGastos = JSON.parse(sessionStorage.getItem('fechasGastos')) || [];
    const diasGastos = fechasGastos.map(fecha => new Date(fecha)); // Convertir a objetos Date

    const fechasIngresos = JSON.parse(sessionStorage.getItem('fechasIngresos')) || [];
    const diasIngresos = fechasIngresos.map(fecha => new Date(fecha)); // Convertir a objetos Date


    // Añadir espacios en blanco antes del primer día del mes
    for (let i = 0; i < firstDay; i++) {
        const emptyDay = document.createElement("div");
        emptyDay.classList.add("empty-day");
        daysContainer.appendChild(emptyDay);
    }

    // Añadir los días del mes
    for (let i = 1; i <= days; i++) {
        const dayEl = document.createElement("div");
        dayEl.textContent = i;
        dayEl.classList.add("day");

        // Verificar si el día actual está en la lista de gastos
        const isGastoDay = diasGastos.some(date =>
            date.getUTCDate() === i &&
            date.getUTCMonth() === currentMonth &&
            date.getUTCFullYear() === currentYear
        );

        if (isGastoDay) {
            dayEl.classList.add("selected-day-gasto");
        }

        // Verificar si el día actual está en la lista de gastos
        const isIngresoDay = diasIngresos.some(date =>
            date.getUTCDate() === i &&
            date.getUTCMonth() === currentMonth &&
            date.getUTCFullYear() === currentYear
        );

        if (isIngresoDay) {
            dayEl.classList.add("selected-day-ingreso");
        }

        daysContainer.appendChild(dayEl);
    }
};


// Manejadores de los botones
prevButton.addEventListener("click", () => {
    if (currentMonth === 0) {
        currentMonth = 11;
        currentYear--;
    } else {
        currentMonth--;
    }
    renderCalendar();
});

nextButton.addEventListener("click", () => {
    if (currentMonth === 11) {
        currentMonth = 0;
        currentYear++;
    } else {
        currentMonth++;
    }
    renderCalendar();
});

// Renderizar calendario inicial
renderCalendar();

