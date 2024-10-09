package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoEgreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.servicios.ServicioEgresoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class ControladorEgreso {

    private ServicioEgreso egresoService;

    // Constructor
    @Autowired
    public ControladorEgreso(ServicioEgreso egresoService) {
        this.egresoService = egresoService;
    }

    // Metodo para obtener todos los egresos
    @GetMapping("/gastos")
    public ModelAndView verEgresos(Integer id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            // Consultar el egreso por ID para ver si existe (esto es opcional si quieres una consulta específica)
            Egreso egreso = egresoService.consultarEgreso(12345.00, id);

            // Si se encuentra, mostrar la vista de egreso
            List<Egreso> listaEgresos = egresoService.getAllEgresos();
            modelAndView.setViewName("gastos");
            modelAndView.addObject("datosEgreso", listaEgresos);
        } catch (RecursoNoEncontrado e) {
            // Si no se encuentra el egreso, mostrar vista de error con el mensaje
            modelAndView.setViewName("gastos");
            modelAndView.addObject("error", e.getMessage());
        }
        return modelAndView;
    }

    // Metodo para crear un nuevo egreso
    @PostMapping("/gastos")
    public ModelAndView crearEgreso(
            @RequestParam("monto") Double monto,
            @RequestParam("fecha") LocalDate fecha,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipoEgreso") TipoEgreso tipoEgreso, HttpServletRequest requestMock) {

        ModelAndView modelAndView = new ModelAndView();

        // Validación de los campos
        if (descripcion == null || descripcion.isEmpty()) {
            modelAndView.addObject("error", "La descripción no puede estar vacía");
            modelAndView.setViewName("error");
            return modelAndView;
        }

        if (monto == null || monto <= 0) {
            modelAndView.addObject("error", "El monto no puede ser nulo o menor a cero");
            modelAndView.setViewName("error");
            return modelAndView;
        }

        // Crear el objeto Egreso
        Egreso egreso = new Egreso(monto, descripcion, fecha);


        // Guardar el egreso
        egresoService.crearEgreso(egreso);

        // Redirigir a la página de gastos
        modelAndView.setViewName("redirect:/gastos");
        return modelAndView;
    }

    // Metodo para ver los detalles de un egreso específico
    @GetMapping("/gasto/detalle")
    public ModelAndView verDetalleEgreso(@RequestParam("monto") double monto, @RequestParam("id") int id) throws RecursoNoEncontrado {
        ModelAndView modelAndView = new ModelAndView();
        Egreso egreso = egresoService.consultarEgreso(monto, id);  // Usar la instancia egresoService

        if (egreso != null) {
            modelAndView.setViewName("detalleEgreso");
            modelAndView.addObject("datosEgreso", egreso);
        } else {
            throw new RecursoNoEncontrado("Egreso no encontrado con monto: " + monto + " e id: " + id);
        }

        return modelAndView;
    }



}




