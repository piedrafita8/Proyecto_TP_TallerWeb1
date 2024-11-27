// JavaScript para actualizar el valor de la categoría seleccionada
let categoriaSeleccionada = ""; // Variable global para guardar la categoría

function seleccionarCategoria(categoria) {
    categoriaSeleccionada = categoria; // Guardar la categoría seleccionada
    document.getElementById("categoriaSeleccionada").value = categoria; // Actualizar el input oculto
}

// Añadir evento a los botones de categoría
var botones = document.getElementsByClassName("btn");
for (var i = 0; i < botones.length; i++) {
    botones[i].addEventListener("click", function () {
        var current = document.getElementsByClassName("active");
        if (current[0]) {
            current[0].className = current[0].className.replace("active", ""); 
        }
        this.className += " active"; 
    });
}

// Función para guardar la fecha del gasto y los datos asociados
function guardarFechaGasto() {
    const fechaSeleccionadaGasto = document.getElementById("fecha").value;
    const monto = parseFloat(document.getElementById("monto").value) || 0;
    const comentario = document.getElementById("comentario").value || ""; // Capturar comentario
    const tipoEgreso = categoriaSeleccionada; // Usar la categoría seleccionada

    if (fechaSeleccionadaGasto && monto > 0 && tipoEgreso) {
        // Guardar fechas de gastos en sessionStorage
        let fechasGastos = JSON.parse(sessionStorage.getItem("fechasGastos")) || [];
        fechasGastos.push(fechaSeleccionadaGasto);
        sessionStorage.setItem("fechasGastos", JSON.stringify(fechasGastos));

        // Actualizar total de egresos
        let totalEgresos = parseFloat(sessionStorage.getItem("totalEgresos")) || 0;
        totalEgresos += monto;
        sessionStorage.setItem("totalEgresos", totalEgresos);

        // Guardar tipos de egresos
        let tiposEgresos = JSON.parse(sessionStorage.getItem("tiposEgresos")) || [];
        tiposEgresos.push({ fecha: fechaSeleccionadaGasto, tipo: tipoEgreso, monto: monto, comentario: comentario });
        sessionStorage.setItem("tiposEgresos", JSON.stringify(tiposEgresos));

        // Llamar a actualizarGrafico para reflejar el cambio inmediato
        if (typeof actualizarGrafico === "function") {
            actualizarGrafico();
        }

        // Confirmar el guardado y limpiar el formulario
        alert("Gasto guardado exitosamente.");
        document.getElementById("monto").value = "";
        document.getElementById("fecha").value = "";
        document.getElementById("comentario").value = "";
        categoriaSeleccionada = ""; 
        var activeButton = document.querySelector(".btn.active");
        if (activeButton) activeButton.classList.remove("active"); 
    } else if ((!fechaSeleccionadaGasto || monto === 0 || !tipoEgreso) && typeof actualizarGrafico === "function") {
        // Validar campos obligatorios
        alert("Por favor, completa todos los campos antes de continuar.");
        return;
    }
}
