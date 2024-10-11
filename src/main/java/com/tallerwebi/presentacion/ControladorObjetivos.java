package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import com.tallerwebi.dominio.interfaces.ServicioObjetivo;
import com.tallerwebi.dominio.models.Objetivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/objetivos")
public class ControladorObjetivos {
    @Autowired
    private ServicioObjetivo servicioObjetivo;

    public String mostrarObjetivos(Model model) {
        List<Objetivo> objetivos = servicioObjetivo.obtenerTodosLosObjetivos();
        model.addAttribute("objetivos", objetivos);
        return "objetivos";
    }

    @PostMapping
    public String crearObjetivo(@RequestParam String nombre,
                                @RequestParam Double montoObjetivo,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaLimite,
                                RedirectAttributes redirectAttributes) {
        try {
            Objetivo nuevoObjetivo = new Objetivo(nombre, montoObjetivo, fechaLimite);
            servicioObjetivo.crearObjetivo(nuevoObjetivo);
            redirectAttributes.addFlashAttribute("mensaje", "Objetivo creado exitosamente");
        } catch (ObjetivoExistente e) {
            redirectAttributes.addFlashAttribute("error", "El objetivo ya existe");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el objetivo: " + e.getMessage());
        }
        return "redirect:/objetivos";
    }

    @PostMapping("/{id}/actualizarMonto")
    public String actualizarMonto(@PathVariable Integer id, @RequestParam Double montoAAgregar, RedirectAttributes redirectAttributes) {
        servicioObjetivo.actualizarObjetivo(id, montoAAgregar);
        redirectAttributes.addFlashAttribute("mensaje", "Monto actualizado exitosamente");
        return "redirect:/objetivos";
    }

    @DeleteMapping("/{id}")
    public String eliminarObjetivo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        servicioObjetivo.eliminarObjetivo(id);
        redirectAttributes.addFlashAttribute("mensaje", "Objetivo eliminado exitosamente");
        return "redirect:/objetivos";
    }
}
