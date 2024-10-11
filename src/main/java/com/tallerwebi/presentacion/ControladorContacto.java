package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorContacto {

    @GetMapping("/contacto")
    public String mostrarContacto() {
        return "contacto";  // Nombre de la vista (sin .html)
    }

}
