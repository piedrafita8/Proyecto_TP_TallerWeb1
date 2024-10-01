package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import com.tallerwebi.dominio.interfaces.ServicioObjetivo;
import com.tallerwebi.dominio.models.Objetivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ControladorObjetivos {
    @Autowired
    private ServicioObjetivo servicioObjetivo;

    // Obtener todos los objetivos
    @GetMapping
    public List<Objetivo> getAllObjetivos() {
        return servicioObjetivo.obtenerTodosLosObjetivos();
    }

    // Crear un nuevo objetivo
    @PostMapping
    public ResponseEntity<Objetivo> crearObjetivo(@RequestBody Objetivo objetivo) {
        try {
            servicioObjetivo.crearObjetivo(objetivo);
            return new ResponseEntity<>(objetivo, HttpStatus.CREATED);
        } catch (ObjetivoExistente e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    // Consultar un objetivo por ID y nombre
    @GetMapping("/{id}/{nombre}")
    public ResponseEntity<Objetivo> consultarObjetivo(@PathVariable Integer id) {
        Objetivo objetivo = servicioObjetivo.consultarObjetivo(id);
        return objetivo != null ? new ResponseEntity<>(objetivo, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Actualizar el monto actual del objetivo
    @PutMapping("/{id}/actualizarMonto")
    public ResponseEntity<Void> actualizarMonto(@PathVariable Integer id, @RequestParam Double montoAAgregar) {
        servicioObjetivo.actualizarObjetivo(id, montoAAgregar);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Eliminar un objetivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarObjetivo(@PathVariable Integer id) {
        servicioObjetivo.eliminarObjetivo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
