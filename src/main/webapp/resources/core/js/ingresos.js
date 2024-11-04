// JavaScript para actualizar el valor de la categoría
function seleccionarCategoria(categoria) {
    document.getElementById("categoriaSeleccionada").value = categoria;
}

var botones = document.getElementsByClassName("btn");
for (var i = 0; i < botones.length; i++) {
    botones[i].addEventListener("click", function (){
        var current = document.getElementsByClassName("active");
        current[0].className = current[0].className.replace("active", "");
        this.className += " active" ;
    });
}

function guardarFechaIngresos() {
    const fechaSeleccionadaIngreso = document.getElementById('fecha').value;
    const monto = parseFloat(document.getElementById('monto').value) || 0;

    if (fechaSeleccionadaIngreso && monto > 0) {
        // Guardar fecha en sessionStorage
        let fechasIngresos = JSON.parse(sessionStorage.getItem('fechasIngresos')) || [];
        fechasIngresos.push(fechaSeleccionadaIngreso);
        sessionStorage.setItem('fechasIngresos', JSON.stringify(fechasIngresos));

        // Actualizar total de egresos
        let totalIngresos = parseFloat(sessionStorage.getItem('totalIngresos')) || 0;
        totalIngresos += monto;
        sessionStorage.setItem('totalIngresos', totalIngresos);

        // Llamar a actualizarGrafico para reflejar el cambio inmediato
        if (typeof actualizarGrafico === "function") {
            actualizarGrafico();
        }
    }
}





