package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import com.tallerwebi.dominio.interfaces.ServicioDeuda;
import com.tallerwebi.dominio.interfaces.ServicioUsuario;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/deudas")
public class ControladorDeuda {

    private final ServicioDeuda servicioDeuda;
    private final ServicioUsuario servicioUsuario;

    public ControladorDeuda(ServicioDeuda servicioDeuda, ServicioUsuario servicioUsuario) {
        this.servicioDeuda = servicioDeuda;
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping
    public String mostrarDeudas(HttpServletRequest request, Model model) {
        Long userId = (Long) request.getSession().getAttribute("id");

        if (userId == null) {
            model.addAttribute("error", "Debe iniciar sesión para acceder a las deudas.");
            return "redirect:/login";
        }

        try {
            List<Deuda> deudasQueDebo = servicioDeuda.obtenerDeudasQueDebo(userId);
            List<Deuda> deudasQueMeDeben = servicioDeuda.obtenerDeudasQueMeDeben(userId);

            model.addAttribute("debo", deudasQueDebo);
            model.addAttribute("medeben", deudasQueMeDeben);
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar las deudas: " + e.getMessage());
        }

        return "deudas";
    }

    @PostMapping
    public String agregarDeuda(@ModelAttribute Deuda deuda, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Long userId = (Long) request.getSession().getAttribute("id");
            if (userId == null) {
                redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para agregar una deuda.");
                return "redirect:/login";
            }

            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(userId);
            deuda.setUsuario(usuario);

            servicioDeuda.agregarDeuda(deuda);
            redirectAttributes.addFlashAttribute("mensaje", "Deuda agregada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar la deuda: " + e.getMessage());
        }
        return "redirect:/deudas";
    }


    @DeleteMapping("/{deudaId}")
    public ResponseEntity<String> eliminarDeuda(@PathVariable Long deudaId) {
        System.out.println("Eliminar "+deudaId);
        try {
            servicioDeuda.eliminarDeuda(deudaId);
            return ResponseEntity.ok("Deuda eliminada exitosamente.");
        } catch (Exception e) {
            System.err.println("error"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error al eliminar la deuda: " + e.getMessage());
        }
}

@PutMapping("/pagar/{deudaId}")
public ResponseEntity<String> marcarComoPagada(@PathVariable Long deudaId) {
    try {
        servicioDeuda.marcarDeudaComoPagada(deudaId);
        return ResponseEntity.ok("Deuda marcada como pagada.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Error al marcar la deuda como pagada: " + e.getMessage());
    }
}
}