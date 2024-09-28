package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class ControladorEgreso {
    @Autowired
    private ServicioEgreso egresoService;

    @GetMapping
    public List<Egreso> getAllEgresos() {
        return egresoService.getAllEgresos();
    }

    @PostMapping
    public ResponseEntity<Egreso> crearEgreso(@RequestBody Egreso egreso) {
        Egreso nuevoEgreso = egresoService.crearEgreso(egreso);
        return new ResponseEntity<>(nuevoEgreso, HttpStatus.CREATED);
    }

    // Otros métodos como PUT, DELETE
}