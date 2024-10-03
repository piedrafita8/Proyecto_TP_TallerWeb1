package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.interfaces.ServicioEgreso;
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

    private final ServicioEgreso egresoService;

    // Constructor
    public ControladorEgreso(ServicioEgreso servicioEgreso) {
        this.egresoService = servicioEgreso;
    }

    // Metodo para obtener todos los egresos
    @GetMapping("/gastos")
    public ModelAndView verEgresos(HttpServletRequest request) {
        List<Egreso> egresos = egresoService.getAllEgresos();
        ModelAndView modelAndView = new ModelAndView("gastos");
        modelAndView.addObject("egresos", egresos);
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
        try {
            egresoService.crearEgreso(egreso);
            return new ModelAndView("confirmacion");
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("nuevo-egreso");
            modelAndView.addObject("error", "Error al registrar el egreso: " + e.getMessage());
            return modelAndView;
        }
    }

    // Metodo para ver los detalles de un egreso específico
    public ModelAndView verDetalleEgreso(@RequestParam double monto, @RequestParam int id, HttpServletRequest request) {
        Egreso egreso = egresoService.consultarEgreso(monto, id);
        ModelAndView modelAndView = new ModelAndView("detalle-egreso");
        modelAndView.addObject("egreso", egreso);
        return modelAndView;
    }
}
