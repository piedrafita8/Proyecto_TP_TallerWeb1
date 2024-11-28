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
            alert("No se pudo marcar la deuda como pagada.");
        }
    })
    .catch(error => {
        console.error("Error en la solicitud:", error);
        alert("Ocurrió un error al intentar marcar la deuda como pagada.");
    });
}

function eliminarDeuda(deudaId) {
    fetch(`/deudas/${deudaId}`, {
        method: "DELETE",
    })
    .then(response => {
        console.log("Respuesta del servidor:", response); // Muestra toda la respuesta
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
            alert("No se pudo eliminar la deuda.");
        }
    })
    .catch(error => {
        console.error("Error en la solicitud:", error);
        alert("Ocurrió un error al eliminar la deuda.");
    });
}