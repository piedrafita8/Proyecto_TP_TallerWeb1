function marcarPagada(deudaId) {
    fetch(`/deudas/pagar/${deudaId}`, {
        method: "PUT",
    }).then(() => window.location.reload());
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