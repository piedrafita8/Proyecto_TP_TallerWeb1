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

function guardarFechaGasto() {
    const fechaSeleccionadaGasto = document.getElementById('fecha').value;

    if (fechaSeleccionadaGasto) {
        // Obtener las fechas existentes del sessionStorage
        let fechasGastos = JSON.parse(sessionStorage.getItem('fechasGastos')) || [];

        console.log("Fecha seleccionada:", fechaSeleccionadaGasto); // Debugging

        // Agregar la nueva fecha
        fechasGastos.push(fechaSeleccionadaGasto);

        console.log("Fechas actuales:", fechasGastos); // Debugging

        // Guardar nuevamente el array en sessionStorage
        sessionStorage.setItem('fechasGastos', JSON.stringify(fechasGastos));
    }
}





