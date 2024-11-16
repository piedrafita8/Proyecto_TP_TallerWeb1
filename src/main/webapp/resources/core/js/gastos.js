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

function guardarFechaGasto() {
    const fechaSeleccionadaGasto = document.getElementById('fecha').value;
    const monto = parseFloat(document.getElementById('monto').value) || 0;

    if (fechaSeleccionadaGasto && monto > 0) {
        // Guardar fecha en sessionStorage
        let fechasGastos = JSON.parse(sessionStorage.getItem('fechasGastos')) || [];
        fechasGastos.push(fechaSeleccionadaGasto);
        sessionStorage.setItem('fechasGastos', JSON.stringify(fechasGastos));

        // Actualizar total de egresos
        let totalEgresos = parseFloat(sessionStorage.getItem('totalEgresos')) || 0;
        totalEgresos += monto;
        sessionStorage.setItem('totalEgresos', totalEgresos);

        // Llamar a actualizarGrafico para reflejar el cambio inmediato
        if (typeof actualizarGrafico === "function") {
            actualizarGrafico();
        }
    }else if(fechaSeleccionadaGasto == null || monto === 0 && typeof actualizarGrafico === "function") {
        // No hace nada si la fecha no se ingresa o si el monto es 0 (no cambia el grafico ni el calendario)
        return;
    }
}







