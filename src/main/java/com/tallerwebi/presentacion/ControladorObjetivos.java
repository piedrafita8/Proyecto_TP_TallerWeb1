package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.CategoriaObjetivo;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.interfaces.ServicioObjetivo;
import com.tallerwebi.dominio.interfaces.ServicioTransaccion;
import com.tallerwebi.dominio.models.Objetivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/objetivos")
public class ControladorObjetivos {
    @Autowired
    private ServicioObjetivo servicioObjetivo;
    private ServicioTransaccion servicioTransaccion;

    @GetMapping
    public String mostrarObjetivosPorUsuario(HttpServletRequest request, Model model) {
        Long userId = (Long) request.getSession().getAttribute("id");

        if (userId == null) {
            model.addAttribute("error", "Debe iniciar sesión para acceder a sus objetivos.");
            return "redirect:/login";
        }

        List<Objetivo> objetivos = servicioObjetivo.obtenerTodosLosObjetivosPorUsuario(userId);
        model.addAttribute("objetivos", objetivos);

        return "objetivos";
    }


    @PostMapping
    public ModelAndView crearObjetivo(@RequestParam String nombre,
                                      @RequestParam Double montoObjetivo,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaLimite,
                                      @RequestParam CategoriaObjetivo categoria, // Nuevo parámetro
                                      RedirectAttributes redirectAttributes,
                                      HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        Long userId = (Long) request.getSession().getAttribute("id");
        if (userId == null) {
            modelAndView.addObject("error", "No se pudo identificar al usuario.");
            return modelAndView;
        }
        try {
            servicioObjetivo.crearObjetivo(nombre, montoObjetivo, fechaLimite, categoria, userId); // Modificar llamada
            redirectAttributes.addFlashAttribute("mensaje", "Objetivo creado exitosamente");
        } catch (ObjetivoExistente e) {
            redirectAttributes.addFlashAttribute("error", "El objetivo ya existe");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el objetivo: " + e.getMessage());
        }

        modelAndView.setViewName("redirect:/objetivos");
        return modelAndView;
    }

    @PostMapping("/{id}/aportar")
    public String aportarAObjetivo(
            @PathVariable Integer id,
            @RequestParam Double montoAportado,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        Long userId = (Long) request.getSession().getAttribute("id");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para realizar un aporte.");
            return "redirect:/login";
        }

        try {
            if (montoAportado <= 0) {
                throw new IllegalArgumentException("El monto a aportar debe ser mayor a cero.");
            }

            // Obtén el último email buscado y la categoría de la sesión
            String ultimoEmailBuscado = (String) request.getSession().getAttribute("ultimoEmailBuscado");
            CategoriaObjetivo ultimaCategoriaBuscada = (CategoriaObjetivo) request.getSession().getAttribute("ultimaCategoriaBuscada");

            servicioObjetivo.aportarAObjetivo(id, montoAportado, userId, ultimoEmailBuscado);

            redirectAttributes.addFlashAttribute("mensaje", "Aporte realizado exitosamente.");

            // Redirige de vuelta al método de búsqueda para mantener los resultados
            if (ultimoEmailBuscado != null || ultimaCategoriaBuscada != null) {
                return "redirect:/buscar-objetivos";
            }
        } catch (SaldoInsuficiente e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al realizar el aporte: " + e.getMessage());
        }

        return "redirect:/index";
    }

    @GetMapping("/index")
    public String mostrarIndex(Model model) {
        model.addAttribute("categorias", CategoriaObjetivo.values());
        return "index";
    }

    @PostMapping("/buscar-objetivos")
    public String buscarObjetivos(
            @RequestParam(required = false) String emailUsuario,
            @RequestParam(required = false) CategoriaObjetivo categoria,
            HttpServletRequest request,
            Model model) {

        // Guardar en sesión para mantener el estado de búsqueda
        request.getSession().setAttribute("ultimoEmailBuscado", emailUsuario);
        request.getSession().setAttribute("ultimaCategoriaBuscada", categoria);

        List<Objetivo> objetivos;

        if ((emailUsuario == null || emailUsuario.isEmpty()) && categoria == null) {
            objetivos = servicioObjetivo.obtenerTodosLosObjetivos();
        } else {
            objetivos = servicioObjetivo.buscarObjetivosPorFiltros(emailUsuario, categoria);
        }

        model.addAttribute("objetivos", objetivos);
        model.addAttribute("categorias", CategoriaObjetivo.values());

        return "index";
    }


    @PostMapping("/{id}/actualizarMonto")
    public String actualizarMonto(@PathVariable Integer id,
                                  @RequestParam Double montoAAgregar,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        Long userId = (Long) request.getSession().getAttribute("id");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para actualizar un objetivo.");
            return "redirect:/login";
        }

        try {
            servicioObjetivo.actualizarObjetivo(id, montoAAgregar, userId);
            redirectAttributes.addFlashAttribute("mensaje", "Monto actualizado exitosamente");
        } catch (SaldoInsuficiente e) {
            redirectAttributes.addFlashAttribute("error", "Saldo insuficiente para actualizar el objetivo.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el objetivo: " + e.getMessage());
        }
        return "redirect:/index";
    }

    @DeleteMapping("/{id}")
    public String eliminarObjetivo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        servicioObjetivo.eliminarObjetivo(id);
        redirectAttributes.addFlashAttribute("mensaje", "Objetivo eliminado exitosamente");
        return "redirect:/objetivos";
    }
}
