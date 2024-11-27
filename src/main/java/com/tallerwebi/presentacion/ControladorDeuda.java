package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.interfaces.ServicioDeuda;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/deudas")
public class ControladorDeuda {

    private final ServicioDeuda servicioDeuda;

    public ControladorDeuda(ServicioDeuda servicioDeuda) {
        this.servicioDeuda = servicioDeuda;
    }

    @GetMapping
    public String mostrarDeudas(HttpServletRequest request, Model model) {
        Long userId = (Long) request.getSession().getAttribute("id");

        if (userId == null) {
            model.addAttribute("error", "Debe iniciar sesión para acceder a las deudas.");
            return "redirect:/login";
        }

        List<Deuda> deudasQueDebo = servicioDeuda.obtenerDeudasQueDebo(userId);
        List<Deuda> deudasQueMeDeben = servicioDeuda.obtenerDeudasQueMeDeben(userId);

        model.addAttribute("debo", deudasQueDebo);
        model.addAttribute("medeben", deudasQueMeDeben);

        return "deudas"; 
    }

    @PostMapping
    public String agregarDeuda(@ModelAttribute Deuda deuda, RedirectAttributes redirectAttributes) {
        try {
            servicioDeuda.agregarDeuda(deuda);
            redirectAttributes.addFlashAttribute("mensaje", "Deuda agregada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar la deuda: " + e.getMessage());
        }
        return "redirect:/deudas";
    }

    @DeleteMapping("/{deudaId}")
    public String eliminarDeuda(@PathVariable Long deudaId, RedirectAttributes redirectAttributes) {
        try {
            servicioDeuda.eliminarDeuda(deudaId);
            redirectAttributes.addFlashAttribute("mensaje", "Deuda eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la deuda: " + e.getMessage());
        }
        return "redirect:/deudas";
    }

    @PutMapping("/pagar/{deudaId}")
    public String marcarComoPagada(@PathVariable Long deudaId, RedirectAttributes redirectAttributes) {
        try {
            servicioDeuda.marcarDeudaComoPagada(deudaId);
            redirectAttributes.addFlashAttribute("mensaje", "Deuda marcada como pagada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al marcar la deuda como pagada: " + e.getMessage());
        }
        return "redirect:/deudas";
    }

}