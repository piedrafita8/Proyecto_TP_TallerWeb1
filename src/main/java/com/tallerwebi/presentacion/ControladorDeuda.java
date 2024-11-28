package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
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


    

    @RequestMapping(value = "/{deudaId}", method = RequestMethod.DELETE)
    public String eliminarDeuda(@PathVariable Long deudaId, RedirectAttributes redirectAttributes) {
        try {
            servicioDeuda.eliminarDeuda(deudaId);
            redirectAttributes.addFlashAttribute("mensaje", "Deuda eliminada exitosamente.");
            return "redirect:/deudas";
        } catch (RecursoNoEncontrado e) {
            redirectAttributes.addFlashAttribute("error", "Deuda no encontrada.");
            return "redirect:/deudas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la deuda.");
            return "redirect:/deudas";
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