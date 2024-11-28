function marcarPagada(deudaId) {
    fetch(`/deudas/pagar/${deudaId}`, {
        method: "PUT",
    })
    .then(response => {
        if (response.ok) {
            window.location.reload(); // Refresca la página solo si todo salió bien
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
    console.log("Intentando eliminar");
    console.log("url:",`/deudas/${deudaId}`);
    fetch(`/deudas/${deudaId}`, {
        method: "DELETE",
    })
    .then(response => {
        if (response.ok) {
            window.location.reload();
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