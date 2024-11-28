function marcarPagada(deudaId) {
    fetch(`/deudas/pagar/${deudaId}`, {
        method: "PUT",
    })
    .then(response => {
        if (response.ok) {
            // Aquí encontramos la fila por su id y actualizamos el checkbox
            const deudaRow = document.querySelector(`#deuda-${deudaId}`);
            const checkbox = deudaRow.querySelector("input[type='checkbox']");
            checkbox.checked = true; // Marca la deuda como pagada en la UI
        } else {
            console.error("Error al marcar como pagada:", response.statusText);
        }
    })
    .catch(error => {
        console.error("Error en la solicitud:", error);
    });
}

function eliminarDeuda(deudaId) {
    fetch(`/deudas/${deudaId}`, {
        method: "DELETE",
    })
    .then(response => {
        
        return response.text();  // Si el servidor responde con un texto, usa .text()
        // Si el servidor responde con un JSON, usa .json()
    })
    .then(responseText => {
        console.log("Cuerpo de la respuesta:", responseText); // Muestra el cuerpo de la respuesta

        if (response.ok) {
            // Si la respuesta fue exitosa, eliminamos la fila en la tabla
            const deudaRow = document.querySelector(`#deuda-${deudaId}`);
            if (deudaRow) {
                deudaRow.remove();
            }
        } else {
            console.error("Error al eliminar la deuda:", response.statusText);
        }
    })
    .catch(error => {
        console.error("Error en la solicitud:", error);
    });
}