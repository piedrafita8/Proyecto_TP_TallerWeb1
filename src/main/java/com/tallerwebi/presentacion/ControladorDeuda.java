package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.interfaces.ServicioDeuda;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deudas")
public class ControladorDeuda {

    private final ServicioDeuda servicioDeuda;

    public ControladorDeuda(ServicioDeuda servicioDeuda) {
        this.servicioDeuda = servicioDeuda;
    }

    @GetMapping("/{userId}")
    public Map<String, List<Deuda>> obtenerTodasLasDeudas(@PathVariable Long userId) {
        List<Deuda> deudasQueDebo = servicioDeuda.obtenerDeudasQueDebo(userId);
        List<Deuda> deudasQueMeDeben = servicioDeuda.obtenerDeudasQueMeDeben(userId);

        Map<String, List<Deuda>> resultado = new HashMap<>();
        resultado.put("debo", deudasQueDebo);
        resultado.put("medeben", deudasQueMeDeben);

        return resultado;
    }

    @PostMapping
    public void agregarDeuda(@RequestBody Deuda deuda) {
        servicioDeuda.agregarDeuda(deuda);
    }

    @DeleteMapping("/{deudaId}")
    public void eliminarDeuda(@PathVariable Long deudaId) {
        servicioDeuda.eliminarDeuda(deudaId);
    }

    @PutMapping("/pagar/{deudaId}")
    public void marcarComoPagada(@PathVariable Long deudaId) {
        servicioDeuda.marcarDeudaComoPagada(deudaId);
    }
}