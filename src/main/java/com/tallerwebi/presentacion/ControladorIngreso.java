package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorIngreso {

    private ServicioIngreso ingresoService;

    public ControladorIngreso(ServicioIngreso ingresoService) {
        this.ingresoService = ingresoService;
    }

    @GetMapping
    public List<Ingreso> todosLosIngresos() {
        return ingresoService.getAllIngresos();
    }

    @PostMapping
    public ResponseEntity<Ingreso> crearIngreso(@RequestBody Ingreso ingreso) {
        Ingreso nuevoIngreso = ingresoService.crearIngreso(ingreso);
        return new ResponseEntity<>(nuevoIngreso, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/validar-ingreso", method = RequestMethod.POST)
    public ModelAndView validarIngreso(DatosIngreso datosIngreso, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        if (datosIngreso.getDescripcion() == null || datosIngreso.getMonto() == null || datosIngreso.getMonto() <= 0) {
            modelAndView.setViewName("redirect:/ingreso"); // Asegúrate de que esto sea correcto
            request.getSession().setAttribute("error", "Por favor, completa la información del ingreso.");
            return modelAndView;
        }

        // Lógica para manejar el ingreso válido
        Ingreso ingresoEncontrado = ingresoService.consultarIngreso(datosIngreso.getMonto(), datosIngreso.getFecha());
        if (ingresoEncontrado != null) {
            modelAndView.setViewName("redirect:/esquema");
            request.getSession().setAttribute(ingresoEncontrado.getDescripcion(), ingresoEncontrado.getDescripcion());
        } else {
            modelAndView.setViewName("redirect:/ingreso"); // Redirigir de nuevo si no se encuentra el ingreso
        }

        return modelAndView;
    }

    // Otros métodos como PUT, DELETE
}