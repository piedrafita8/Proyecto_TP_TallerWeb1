function marcarPagada(deudaId) {
    fetch(`/deudas/pagar/${deudaId}`, {
        method: "PUT",
    }).then(() => window.location.reload());
}

function eliminarDeuda(deudaId) {
    fetch(`/deudas/${deudaId}`, {
        method: "DELETE",
    }).then(() => window.location.reload());
}