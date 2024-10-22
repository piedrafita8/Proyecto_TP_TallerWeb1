// JavaScript para actualizar el valor de la categoría
function seleccionarCategoria(categoria) {
    document.getElementById("categoriaSeleccionada").value = categoria;
}

var categorias = document.getElementById("categorias");
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

    if (fechaSeleccionadaIngreso) {
        // Obtener las fechas existentes del sessionStorage
        let fechasIngresos = JSON.parse(sessionStorage.getItem('fechasIngresos')) || [];

        console.log("Fecha seleccionada:", fechaSeleccionadaIngreso); // Debugging

        // Agregar la nueva fecha
        fechasIngresos.push(fechaSeleccionadaIngreso);

        console.log("Fechas actuales:", fechasIngresos); // Debugging

        // Guardar nuevamente el array en sessionStorage
        sessionStorage.setItem('fechasIngresos', JSON.stringify(fechasIngresos));
    }
}





