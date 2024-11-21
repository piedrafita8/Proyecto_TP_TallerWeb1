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
            <button class="delete-goal-btn" onclick="handlerEliminarObjetivo(${objetivo.id})">Eliminar Objetivo</button>
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

//Handler para eliminar objetivo
function handlerEliminarObjetivo(idObjetivo) {
  objetivos = objetivos.filter((objetivo) => objetivo.id !== idObjetivo);
  mostrarObjetivos();
}