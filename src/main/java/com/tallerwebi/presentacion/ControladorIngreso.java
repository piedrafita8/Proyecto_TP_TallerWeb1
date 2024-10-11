package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    public ModelAndView verIngresos(Integer id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        // Consultar el egreso por ID para ver si existe (esto es opcional si quieres una consulta específica)
        try {
            ingresoService.consultarIngreso(173345.00, id);
            List<Ingreso> listaIngresos = ingresoService.getAllIngresos();
            modelAndView.setViewName("ingreso");
            modelAndView.addObject("datosIngreso", listaIngresos);
        } catch (RecursoNoEncontrado e) {
            // Si no se encuentra el ingreso, mostrar vista de error con el mensaje
            modelAndView.setViewName("ingreso");
            modelAndView.addObject("error", e.getMessage());
        }
        return modelAndView;
    }

    // Crear un nuevo ingreso (formato JSON)
    @PostMapping("/ingreso")
    public ResponseEntity<Ingreso> crearIngreso(@RequestBody Ingreso ingreso) {
        Ingreso nuevoIngreso = ingresoService.crearIngreso(ingreso);
        return new ResponseEntity<>(nuevoIngreso, HttpStatus.CREATED);
    }

    // Validar un ingreso y redirigir según el resultado
    @PostMapping("/validar-ingreso")
    public ModelAndView validarIngreso(DatosIngreso datosIngreso, HttpServletRequest request) throws RecursoNoEncontrado {
        ModelAndView modelAndView = new ModelAndView();

        if (datosIngreso.getDescripcion() == null || datosIngreso.getMonto() == null || datosIngreso.getMonto() <= 0) {
            modelAndView.setViewName("redirect:/ingreso");
            request.getSession().setAttribute("error", "Por favor, completa la información del ingreso.");
            return modelAndView;
        }

        Ingreso ingresoEncontrado = ingresoService.consultarIngreso(datosIngreso.getMonto(), datosIngreso.getId());
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
