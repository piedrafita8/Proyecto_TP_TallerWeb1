package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.interfaces.ServicioEgreso;
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
import java.util.List;

@Controller
public class ControladorEgreso {

    private ServicioEgreso egresoService;

    // Constructor
    @Autowired
    public ControladorEgreso(ServicioEgreso egresoService) {
        this.egresoService = egresoService;
    }

//    @GetMapping("/")
//    public ModelAndView redirigirARaiz() {
//        return new ModelAndView("redirect:/gastos");
//    }

    // Metodo para obtener todos los egresos
    @GetMapping("/gastos")
    public ModelAndView verEgresos(Integer id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            // Consultar el egreso por ID para ver si existe (esto es opcional si quieres una consulta específica)
            Egreso egreso = egresoService.consultarEgreso(12345.00, id);

            // Si se encuentra, mostrar la vista de egreso
            List<Egreso> listaEgresos = egresoService.getAllEgresos();
            modelAndView.setViewName("egreso");
            modelAndView.addObject("datosEgreso", listaEgresos);
        } catch (RecursoNoEncontrado e) {
            // Si no se encuentra el egreso, mostrar vista de error con el mensaje
            modelAndView.setViewName("errorEgreso");
            modelAndView.addObject("error", e.getMessage());
        }
        return modelAndView;
    }

    // Metodo para crear un nuevo egreso
    @PostMapping("/gastos")
    public ResponseEntity<Egreso> crearEgreso(@RequestBody Egreso egreso) {
        Egreso nuevoEgreso = egresoService.crearEgreso(egreso);
        return new ResponseEntity<>(nuevoEgreso, HttpStatus.CREATED);
    }

    // Metodo para registrar un nuevo egreso y redirigir
    public ModelAndView registrarEgreso(Egreso egreso, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        // Verificar si la descripción está vacía
        if (egreso.getDescripcion() == null || egreso.getDescripcion().isEmpty()) {
            modelAndView.addObject("error", "La descripción no puede estar vacía");
            modelAndView.setViewName("error");
            return modelAndView;
        }

        // Verificar si la descripción está vacía
        if (egreso.getMonto() == null) {
            modelAndView.addObject("error", "El monto no puede estar vacío");
            modelAndView.setViewName("error");
            return modelAndView;
        }

        // Lógica para registrar el egreso (se omite en este caso)
        egresoService.crearEgreso(egreso);
        modelAndView.setViewName("exito");
        return modelAndView;
    }

    // Metodo para ver los detalles de un egreso específico
    public ModelAndView verDetalleEgreso(@RequestParam double monto, @RequestParam int id, HttpServletRequest request) throws RecursoNoEncontrado {
        Egreso listaEgresos = egresoService.consultarEgreso(monto, id);
        ModelAndView modelAndView = new ModelAndView("egresos");
        modelAndView.addObject("datosEgreso", listaEgresos);
        return modelAndView;
    }
}
