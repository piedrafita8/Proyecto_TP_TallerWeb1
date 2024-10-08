package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorIngreso {

    private final ServicioIngreso ingresoService;

    public ControladorIngreso(ServicioIngreso ingresoService) {
        this.ingresoService = ingresoService;
    }

    // Metodo para manejar la vista de ingresos y agregar datos al modelo
    @GetMapping("/ingreso")
    public ModelAndView mostrarIngreso() {
        ModelAndView modelAndView = new ModelAndView("ingreso");
        modelAndView.addObject("datosIngreso", new DatosIngreso()); // Añade un objeto vacío de DatosIngreso al modelo
        modelAndView.addObject("listaIngresos", ingresoService.getAllIngresos()); // Opcionalmente, añade la lista de ingresos
        return modelAndView;
    }

    @PostMapping("/ingreso")
    public ResponseEntity<Ingreso> crearIngreso(@RequestBody Ingreso ingreso) {
        Ingreso nuevoIngreso = ingresoService.crearIngreso(ingreso);
        return new ResponseEntity<>(nuevoIngreso, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/validar-ingreso", method = RequestMethod.POST)
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

    public ModelAndView registrarIngreso(Ingreso datosIngreso, HttpServletRequest requestMock) {
        ModelAndView modelAndView = new ModelAndView();

        // Validaciones de los datos del egreso (puedes adaptarlo a tus requisitos específicos)
        if (datosIngreso == null || datosIngreso.getMonto() == null || datosIngreso.getMonto() <= 0 || datosIngreso.getDescripcion() == null || datosIngreso.getDescripcion().isEmpty()) {
            // Si los datos son inválidos, redirigir de vuelta al formulario de egresos con un mensaje de error
            modelAndView.setViewName("redirect:/ingreso"); // Reemplaza con la vista que necesites
            requestMock.getSession().setAttribute("error", "Por favor, completa todos los campos del ingreso de manera correcta.");
            return modelAndView;
        }

        // Intentar registrar el egreso
        try {
            Ingreso registrarIngreso = ingresoService.registrarIngreso(datosIngreso);

            // Si se registra correctamente, redirigir a la vista de esquema o mostrar lista de egresos
            modelAndView.setViewName("redirect:/esquema");  // Reemplaza con la vista que necesites
            requestMock.getSession().setAttribute("mensajeExito", "Ingreso registrado exitosamente: " + registrarIngreso.getDescripcion());

        } catch (RecursoNoEncontrado e) {
            // Si ocurre un error de recurso no encontrado, manejar la excepción y redirigir con mensaje de error
            modelAndView.setViewName("redirect:/ingreso"); // Reemplaza con la vista que necesites
            requestMock.getSession().setAttribute("error", "Error al registrar el ingreso. Recurso no encontrado.");
        }

        return modelAndView;
    }

    // Otros métodos como PUT, DELETE
}
