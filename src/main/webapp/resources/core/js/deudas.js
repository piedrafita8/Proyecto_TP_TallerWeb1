function marcarPagada(deudaId) {
    fetch(`/deudas/pagar/${deudaId}`, {
        method: "PUT",
    })
    .then(response => {
        if (response.ok) {
            
            const deudaRow = document.querySelector(`#deuda-${deudaId}`);
            const checkbox = deudaRow.querySelector("input[type='checkbox']");
            checkbox.checked = true; 
        }
    })
}

function eliminarDeuda(deudaId) {
    fetch(`/deudas/${deudaId}`, {
        method: "DELETE",
    })
    .then(response => {
        
        return response.text();  
    })
    .then(responseText => {
        console.log("Cuerpo de la respuesta:", responseText); 

        if (response.ok) {
            
            const deudaRow = document.querySelector(`#deuda-${deudaId}`);
            if (deudaRow) {
                deudaRow.remove();
            }
        }
    });
}