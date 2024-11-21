package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoEgreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ControladorEgreso {

    private ServicioEgreso egresoService;

    // Constructor
    @Autowired
    public ControladorEgreso(ServicioEgreso egresoService) {
        this.egresoService = egresoService;
    }

    // Mostrar la vista de egreso
    @GetMapping("/gastos")
    public String mostrarEgreso(Model model) {
        model.addAttribute("datosEgreso", new DatosIngreso());
        return "gastos";
    }

    // Metodo para obtener todos los egresos
    @GetMapping("api/gastos")
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

    @PostMapping("/gastos")
    public ModelAndView crearEgreso(
            @RequestParam("monto") Double monto,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipoEgreso") TipoEgreso tipoEgreso, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();

        if (descripcion == null || descripcion.isEmpty()) {
            modelAndView.addObject("error", "La descripción no puede estar vacía");
            return modelAndView;
        }

        if (monto == null || monto <= 0) {
            modelAndView.addObject("error", "El monto no puede ser nulo o menor a cero");
            return modelAndView;
        }

        if(fecha == null){
            modelAndView.addObject("error", "La fecha no puede ser nula");
            return modelAndView;
        }

        if(tipoEgreso == null){
            modelAndView.addObject("error", "El tipo de egreso no puede ser nulo");
            return modelAndView;
        }

        Long userId = (Long) request.getSession().getAttribute("id");
        if (userId == null) {
            modelAndView.addObject("error", "No se pudo identificar al usuario.");
            return modelAndView;
        }

        Egreso egreso = new Egreso();
        egreso.setMonto(monto);
        egreso.setFecha(fecha);
        egreso.setDescripcion(descripcion);
        egreso.setTipoEgreso(tipoEgreso);

        try {

            egresoService.crearEgreso(egreso, userId);

            modelAndView.setViewName("redirect:/gastos");
        } catch (SaldoInsuficiente e) {
            modelAndView.setViewName("gastos");
            modelAndView.addObject("error", "Saldo insuficiente para realizar el egreso.");
        }

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




