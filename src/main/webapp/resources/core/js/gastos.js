
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



