// Array para guardar todos los objetivos
let objetivos = [];

// Function para crear un objetivo
function crearObjetivo(nombre, dineroDelObjetivo) {
  const nuevoObjetivo = {
    id: objetivos.length + 1,
    nombre: nombre,
    dineroObjetivo: parseFloat(dineroDelObjetivo),
    dineroActual: 0,
  };
  objetivos.push(nuevoObjetivo);
  mostrarObjetivos();
}

// Function para actualizar el progreso de un objetivo
function actualizarObjetivo(idObjetivo, monto) {
  const objetivo = objetivos.find((o) => o.id === idObjetivo);
  if (objetivo) {
    objetivo.dineroActual += parseFloat(monto);
    if (objetivo.dineroActual > objetivo.dineroObjetivo) {
      objetivo.dineroActual = objetivo.dineroObjetivo;
    }
    mostrarObjetivos();
  }
}

// Function para mostrar todos los objetivos en la vista
function mostrarObjetivos() {
  const listaObjetivos = document.querySelector(".goals-list");
  listaObjetivos.innerHTML = "";

  objetivos.forEach((objetivo) => {
    const objetivoItem = document.createElement("li");
    objetivoItem.className = "goal-item";
    objetivoItem.innerHTML = `
            <div class="goal-name">${objetivo.nombre}</div>
            <div class="goal-progress">
                <progress value="${objetivo.dineroActual}" max="${
      objetivo.dineroObjetivo
    }"></progress>
                <span>$${objetivo.dineroActual.toFixed(
                  2
                )} / $${objetivo.dineroObjetivo.toFixed(2)}</span>
            </div>
            <form class="add-money-form" onsubmit="handlerAgregarDinero(event, ${
              objetivo.id
            })">
                <input type="number" placeholder="Nuevo Monto" required>
                <button type="submit">Agregar dinero</button>
            </form>
        `;
    listaObjetivos.appendChild(objetivoItem);
  });
}

// Handler para agregar un objetivo
function handlerAgregarObjetivo(event) {
  event.preventDefault();
  const nombreInput = document.querySelector(
    '.add-goal-form input[type="text"]'
  );
  const montoInput = document.querySelector(
    '.add-goal-form input[type="number"]'
  );

  if (nombreInput.value && montoInput.value) {
    crearObjetivo(nombreInput.value, montoInput.value);
    nombreInput.value = "";
    montoInput.value = "";
  }
}

// Handler para actualizar el dinero de un objetivo
function handlerAgregarDinero(event, idObjetivo) {
  event.preventDefault();
  const montoInput = event.target.querySelector('input[type="number"]');
  if (montoInput.value) {
    actualizarObjetivo(idObjetivo, montoInput.value);
    montoInput.value = "";
  }
}

// Iniciarlizar la vista
function init() {
  const agregarObjetivoForm = document.querySelector(".add-goal-form");
  agregarObjetivoForm.addEventListener("submit", handlerAgregarObjetivo);

  // Mocks
  crearObjetivo("Nuevo auto", 10000);
  crearObjetivo("Vacaciones", 3000);
}

// Correr la Init Function cuando se termine de cargar el DOM
document.addEventListener("DOMContentLoaded", init);


//barra de progreso
document.addEventListener("DOMContentLoaded", () => {
  const formsActualizar = document.querySelectorAll("form[th\\:action*='actualizarMonto']");

  formsActualizar.forEach((form) => {
    form.addEventListener("submit", (event) => {
      event.preventDefault(); // Evitar recarga de página

      const montoAAgregar = parseFloat(form.querySelector("input[name='montoAAgregar']").value);
      const containerTxt = form.closest(".container-txt");

      // Obtener monto actual y objetivo
      const montoActualEl = containerTxt.querySelector("span[th\\:text*='montoActual']");
      const montoObjetivoEl = containerTxt.querySelector("span[th\\:text*='montoObjetivo']");

      const montoActual = parseFloat(montoActualEl.textContent.replace(",", ""));
      const montoObjetivo = parseFloat(montoObjetivoEl.textContent.replace(",", ""));

      // Actualizar el monto actual
      const nuevoMontoActual = Math.min(montoActual + montoAAgregar, montoObjetivo); // Limitar al monto objetivo
      montoActualEl.textContent = nuevoMontoActual.toFixed(2);

      // Calcular el porcentaje de la barra
      const porcentaje = (nuevoMontoActual / montoObjetivo) * 100;

      // Actualizar visualmente la barra
      const progressBar = containerTxt.nextElementSibling.querySelector(".progress-bar::after");
      progressBar.style.width = `${porcentaje}%`;

      // Opcional: Deshabilitar el botón si se alcanza el objetivo
      if (nuevoMontoActual >= montoObjetivo) {
        form.querySelector("button").disabled = true;
      }
    });
  });
});