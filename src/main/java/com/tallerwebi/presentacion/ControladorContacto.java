package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorContacto {

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";  // Esto hace referencia a contacto.html dentro de /templates.
    }

}
