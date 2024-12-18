package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.CategoriaObjetivo;
import com.tallerwebi.dominio.interfaces.*;
import com.tallerwebi.dominio.models.*;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;
    private ServicioTransaccion servicioTransaccion;
    private ServicioUsuario servicioUsuario;
    private ServicioObjetivo servicioObjetivo;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin, ServicioTransaccion servicioTransaccion, ServicioUsuario servicioUsuario, ServicioObjetivo servicioObjetivo){
        this.servicioLogin = servicioLogin;
        this.servicioTransaccion = servicioTransaccion;
        this.servicioUsuario = servicioUsuario;
        this.servicioObjetivo = servicioObjetivo;
    }

    @RequestMapping("/login")
    public ModelAndView irALogin() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getUsername(), datosLogin.getPassword());
        if (usuarioBuscado != null) {
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            request.getSession().setAttribute("id", usuarioBuscado.getId());
            return new ModelAndView("redirect:/index");
        }
        else {
            model.put("error", "Usuario o clave incorrecta");
        }
        return new ModelAndView("login", model);
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario) {
        ModelMap model = new ModelMap();
        try{
            servicioLogin.registrar(usuario);
        } catch (UsuarioExistente e){
            model.put("error", "El usuario ya existe");
            return new ModelAndView("nuevo-usuario", model);
        } catch (Exception e){
            model.put("error", "Error al registrar el nuevo usuario");
            return new ModelAndView("nuevo-usuario", model);
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("nuevo-usuario", model);
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public ModelAndView irAHome(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");

        Long userId = (Long) request.getSession().getAttribute("id");

        if (userId == null) {
            modelAndView.setViewName("redirect:/login");
            modelAndView.addObject("error", "Debe iniciar sesión para acceder a esta página.");
            return modelAndView;
        }

        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(userId);
        Double saldo = (usuario != null) ? usuario.getSaldo() : 0.0;

        CategoriaObjetivo[] categorias = CategoriaObjetivo.values();

        // Obtener todas las transacciones del usuario
        List<Transaccion> transacciones = servicioTransaccion.obtenerTodasLasTransaccionesPorUserId(userId);

        // Filtrar ingresos y sumar montos
        Double totalIngresos = transacciones.stream()
                .filter(t -> t instanceof Ingreso)
                .mapToDouble(Transaccion::getMonto)
                .sum();

        // Filtrar ingresos y sumar montos
        Double totalEgresos = transacciones.stream()
                .filter(t -> t instanceof Egreso)
                .mapToDouble(Transaccion::getMonto)
                .sum();

        // Obtener los objetivos personales del usuario
        List<Objetivo> objetivosPersonales = servicioObjetivo.obtenerTodosLosObjetivosPorUsuario(userId);

        // Obtener los objetivos a los que ha aportado
        List<Objetivo> objetivosAportados = servicioObjetivo.obtenerObjetivosAportados(userId);

        modelAndView.addObject("saldo", saldo);
        modelAndView.addObject("transacciones", transacciones);
        modelAndView.addObject("montoIngreso", totalIngresos);
        modelAndView.addObject("montoEgreso", totalEgresos);
        modelAndView.addObject("categorias", categorias);
        modelAndView.addObject("objetivosPersonales", objetivosPersonales);
        modelAndView.addObject("objetivosAportados", objetivosAportados);

        return modelAndView;
    }

    @PostMapping("/buscar-objetivos")
    public ModelAndView buscarObjetivos(
            @RequestParam(required = false) String emailUsuario,
            @RequestParam(required = false) CategoriaObjetivo categoria,
            HttpServletRequest request
    ) {
        ModelAndView modelAndView = new ModelAndView("index");

        // Obtener el usuario de la sesión
        Long userId = (Long) request.getSession().getAttribute("id");
        if (userId == null) {
            modelAndView.setViewName("redirect:/login");
            modelAndView.addObject("error", "Debe iniciar sesión para acceder a esta página.");
            return modelAndView;
        }

        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(userId);

        // Agregar todas las categorías al modelo
        CategoriaObjetivo[] categorias = CategoriaObjetivo.values();

        // Obtener todas las transacciones del usuario
        List<Transaccion> transacciones = servicioTransaccion.obtenerTodasLasTransaccionesPorUserId(userId);

        // Calcular ingresos y egresos
        Double totalIngresos = transacciones.stream()
                .filter(t -> t instanceof Ingreso)
                .mapToDouble(Transaccion::getMonto)
                .sum();

        Double totalEgresos = transacciones.stream()
                .filter(t -> t instanceof Egreso)
                .mapToDouble(Transaccion::getMonto)
                .sum();

        // Filtrar objetivos según los parámetros
        List<Objetivo> objetivosFiltrados = servicioObjetivo.buscarObjetivosPorFiltros(emailUsuario, categoria);

        // Obtener objetivos relacionados con el usuario
        List<Objetivo> objetivosAportados = servicioObjetivo.obtenerObjetivosAportados(userId);
        List<Objetivo> objetivosPersonales = servicioObjetivo.obtenerTodosLosObjetivosPorUsuario(userId);

        // Agregar datos al modelo
        modelAndView.addObject("saldo", usuario.getSaldo());
        modelAndView.addObject("transacciones", transacciones);
        modelAndView.addObject("montoIngreso", totalIngresos);
        modelAndView.addObject("montoEgreso", totalEgresos);
        modelAndView.addObject("categorias", categorias);
        modelAndView.addObject("objetivos", objetivosFiltrados != null ? objetivosFiltrados : Collections.emptyList());
        modelAndView.addObject("objetivosPersonales", objetivosPersonales);
        modelAndView.addObject("objetivosAportados", objetivosAportados);

        return modelAndView;
    }





    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}

