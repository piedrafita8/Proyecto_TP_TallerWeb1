package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.models.Egreso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorEgreso {

    private final ServicioEgreso servicioEgreso;

    public ControladorEgreso(ServicioEgreso servicioEgreso) {
        this.servicioEgreso = servicioEgreso;
    }

    // Metodo para manejar la vista de ingresos y agregar datos al modelo
    @GetMapping("/egreso")
    public ModelAndView mostrarEgreso() {
        ModelAndView modelAndView = new ModelAndView("egreso");
        modelAndView.addObject("datosEgreso", new DatosEgreso()); // Añade un objeto vacío de DatosEgreso al modelo
        modelAndView.addObject("listaEgresos", servicioEgreso.getAllEgresos()); // Opcionalmente, añade la lista de egresos
        return modelAndView;
    }

    @PostMapping("/egreso")
    public ResponseEntity<Egreso> crearEgreso(@RequestBody Egreso egreso) {
        Egreso nuevoEgreso = servicioEgreso.crearEgreso(egreso);
        return new ResponseEntity<>(nuevoEgreso, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/validar-egreso", method = RequestMethod.POST)
    public ModelAndView validarEgreso(DatosEgreso datosEgreso, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        if (datosEgreso.getDescripcion() == null || datosEgreso.getMonto() == null || datosEgreso.getMonto() <= 0) {
            modelAndView.setViewName("redirect:/egreso");
            request.getSession().setAttribute("error", "Por favor, completa la información del egreso.");
            return modelAndView;
        }

        Egreso egresoEncontrado = servicioEgreso.consultarEgreso(datosEgreso.getMonto(), datosEgreso.getFecha());
        if (egresoEncontrado != null) {
            modelAndView.setViewName("redirect:/esquema");
            request.getSession().setAttribute(egresoEncontrado.getDescripcion(), egresoEncontrado.getDescripcion());
        } else {
            modelAndView.setViewName("redirect:/egreso");
        }

        return modelAndView;
    }

    public ModelAndView registrarEgreso(Egreso datosEgreso, HttpServletRequest requestMock) {
        ModelAndView modelAndView = new ModelAndView();

        // Validaciones de los datos del egreso (puedes adaptarlo a tus requisitos específicos)
        if (datosEgreso == null || datosEgreso.getMonto() == null || datosEgreso.getMonto() <= 0 || datosEgreso.getDescripcion() == null || datosEgreso.getDescripcion().isEmpty()) {
            // Si los datos son inválidos, redirigir de vuelta al formulario de egresos con un mensaje de error
            modelAndView.setViewName("redirect:/egreso"); // Reemplaza con la vista que necesites
            requestMock.getSession().setAttribute("error", "Por favor, completa todos los campos del egreso de manera correcta.");
            return modelAndView;
        }

        // Intentar registrar el egreso
        try {
            Egreso registrarEgreso = servicioEgreso.registrarEgreso(datosEgreso);

            // Si se registra correctamente, redirigir a la vista de esquema o mostrar lista de egresos
            modelAndView.setViewName("redirect:/esquema");  // Reemplaza con la vista que necesites
            requestMock.getSession().setAttribute("mensajeExito", "Egreso registrado exitosamente: " + registrarEgreso.getDescripcion());

        } catch (RecursoNoEncontrado e) {
            // Si ocurre un error de recurso no encontrado, manejar la excepción y redirigir con mensaje de error
            modelAndView.setViewName("redirect:/egreso"); // Reemplaza con la vista que necesites
            requestMock.getSession().setAttribute("error", "Error al registrar el egreso. Recurso no encontrado.");
        }

        return modelAndView;
    }

    // Otros métodos como PUT, DELETE
}
