package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorIngreso {

    private final ServicioIngreso ingresoService;

    public ControladorIngreso(ServicioIngreso ingresoService) {
        this.ingresoService = ingresoService;
    }

    // Mostrar la vista de ingreso
    @GetMapping("/ingreso")
    public String mostrarIngreso(Model model) {
        model.addAttribute("datosIngreso", new DatosIngreso());
        return "ingreso";
    }

    // Metodo separado para obtener todos los ingresos en formato JSON
    @GetMapping("/api/ingresos")
    @ResponseBody
    public List<Ingreso> todosLosIngresos() {
        return ingresoService.getAllIngresos();
    }

    // Crear un nuevo ingreso (formato JSON)
    @PostMapping("/ingreso")
    public ResponseEntity<Ingreso> crearIngreso(@RequestBody Ingreso ingreso) {
        Ingreso nuevoIngreso = ingresoService.crearIngreso(ingreso);
        return new ResponseEntity<>(nuevoIngreso, HttpStatus.CREATED);
    }

    // Validar un ingreso y redirigir según el resultado
    @PostMapping("/validar-ingreso")
    public ModelAndView validarIngreso(DatosIngreso datosIngreso, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        if (datosIngreso.getDescripcion() == null || datosIngreso.getMonto() == null || datosIngreso.getMonto() <= 0) {
            modelAndView.setViewName("redirect:/ingreso");
            request.getSession().setAttribute("error", "Por favor, completa la información del ingreso.");
            return modelAndView;
        }

        Ingreso ingresoEncontrado = ingresoService.consultarIngreso(datosIngreso.getMonto(), datosIngreso.getFecha());
        if (ingresoEncontrado != null) {
            modelAndView.setViewName("redirect:/esquema");
            request.getSession().setAttribute(ingresoEncontrado.getDescripcion(), ingresoEncontrado.getDescripcion());
        } else {
            modelAndView.setViewName("redirect:/ingreso");
        }

        return modelAndView;
    }

    // Otros métodos como PUT, DELETE podrían agregarse aquí si es necesario
}
