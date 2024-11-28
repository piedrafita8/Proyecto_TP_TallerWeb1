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
        if (response.ok) {
            // Aquí encontramos la fila por su id y la eliminamos
            const deudaRow = document.querySelector(`#deuda-${deudaId}`);
            deudaRow.remove(); // Elimina la fila de la tabla
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