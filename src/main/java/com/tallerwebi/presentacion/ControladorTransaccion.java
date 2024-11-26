package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoEgreso;
import com.tallerwebi.dominio.enums.TipoIngreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.interfaces.ServicioTransaccion;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.models.Transaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.dsig.TransformService;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ControladorTransaccion {

    private ServicioTransaccion servicioTransaccion;

    @GetMapping("/gastos")
    public String mostrarGastos(Model model) {
        model.addAttribute("datosTransaccion", new DatosTransaccion()); // Agrega el atributo esperado
        return "gastos"; // Nombre de la vista
    }

    @GetMapping("/ingreso")
    public String mostrarIngreso(Model model) {
        model.addAttribute("datosTransaccion", new DatosTransaccion()); // Agrega el atributo esperado
        return "ingreso"; // Nombre de la vista
    }

    @Autowired
    public ControladorTransaccion(ServicioTransaccion servicioTransaccion) {
        this.servicioTransaccion = servicioTransaccion;
    }

    // Metodo para obtener todas las transacciones
    @GetMapping("/api/transacciones")
    public ModelAndView verTransacciones(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Long userId = (Long) request.getSession().getAttribute("id");

        if (userId == null) {
            modelAndView.addObject("error", "No se pudo identificar al usuario.");
            modelAndView.setViewName("index");
            return modelAndView;
        }

        List<Transaccion> listaTransaccion = servicioTransaccion.getTransaccionPorUserId(userId);
        modelAndView.setViewName("index"); // Asumiendo que "index" es la vista principal
        modelAndView.addObject("datosTransaccion", listaTransaccion);

        return modelAndView;
    }

    @PostMapping("/gastos")
    public ModelAndView crearEgreso(
            @RequestParam("monto") Double monto,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipoEgreso") TipoEgreso tipoEgreso,
            HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("gastos"); // Vista por defecto en caso de error

        // Validaciones
        if (descripcion == null || descripcion.isEmpty()) {
            modelAndView.addObject("error", "La descripción no puede estar vacía");
            return modelAndView;
        }

        if (monto == null || monto <= 0) {
            modelAndView.addObject("error", "El monto debe ser mayor a cero");
            return modelAndView;
        }

        if (fecha == null) {
            modelAndView.addObject("error", "La fecha no puede ser nula");
            return modelAndView;
        }

        if (tipoEgreso == null) {
            modelAndView.addObject("error", "El tipo de egreso no puede ser nulo");
            return modelAndView;
        }

        // Verificar sesión del usuario
        Long userId = (Long) request.getSession().getAttribute("id");
        if (userId == null) {
            modelAndView.addObject("error", "No se pudo identificar al usuario");
            return modelAndView;
        }

        // Crear el objeto Egreso
        Egreso egreso = new Egreso();
        egreso.setMonto(monto);
        egreso.setFecha(fecha);
        egreso.setDescripcion(descripcion);
        egreso.setTipoEgreso(tipoEgreso);
        egreso.setUserId(userId);

        // Procesar la transacción
        try {
            servicioTransaccion.crearTransaccion(egreso, userId);
            modelAndView.setViewName("redirect:/gastos"); // Redirigir si es exitoso
        } catch (SaldoInsuficiente e) {
            modelAndView.addObject("error", "Saldo insuficiente para realizar el egreso");
        } catch (Exception e) {
            modelAndView.addObject("error", "Ocurrió un error al procesar la transacción: " + e.getMessage());
        }

        return modelAndView;
    }


    @PostMapping("/ingreso")
    public ModelAndView crearIngreso(
            @RequestParam("monto") Double monto,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipoIngreso") TipoIngreso tipoIngreso,
            HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("ingreso"); // Vista por defecto en caso de error

        // Validaciones de los parámetros
        if (descripcion == null || descripcion.isEmpty()) {
            modelAndView.addObject("error", "La descripción no puede estar vacía");
            return modelAndView;
        }

        if (monto == null || monto <= 0) {
            modelAndView.addObject("error", "El monto debe ser mayor a cero");
            return modelAndView;
        }

        if (fecha == null) {
            modelAndView.addObject("error", "La fecha es requerida");
            return modelAndView;
        }

        if (tipoIngreso == null) {
            modelAndView.addObject("error", "El tipo de ingreso es requerido");
            return modelAndView;
        }

        // Validar usuario
        Long userId = (Long) request.getSession().getAttribute("id");
        if (userId == null) {
            modelAndView.addObject("error", "No se pudo identificar al usuario");
            return modelAndView;
        }

        // Crear el ingreso y asociarlo al usuario
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(monto);
        ingreso.setFecha(fecha);
        ingreso.setDescripcion(descripcion);
        ingreso.setTipoIngreso(tipoIngreso);
        ingreso.setUserId(userId);

        try {
            servicioTransaccion.crearTransaccion(ingreso, userId);
            modelAndView.setViewName("redirect:/ingreso"); // Redirigir si el ingreso fue exitoso
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
        }

        return modelAndView;
    }


    // Metodo para ver los detalles de una transaccion específico
    @GetMapping("/transaccion/detalle")
    public ModelAndView verDetalleTransaccion(@RequestParam("monto") Double monto,
                                              @RequestParam("id") Integer id) throws RecursoNoEncontrado {
        ModelAndView modelAndView = new ModelAndView();
        Transaccion transaccion = servicioTransaccion.consultarTransaccion(monto, id);  // Usar la instancia ingresoService

        if (transaccion != null) {
            modelAndView.setViewName("detalleTransaccion");
            modelAndView.addObject("datosTransaccion", transaccion);
        } else {
            throw new RecursoNoEncontrado("Transaccion no encontrado con monto: " + monto + " e id: " + id);
        }

        return modelAndView;
    }
}
