package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.interfaces.ServicioLogin;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorLogin {

    private ServicioLogin ServicioLogin;
    private ServicioEgreso servicioEgreso;

    @Autowired
    public ControladorLogin(ServicioLogin ServicioLogin, ServicioEgreso servicioEgreso){
        this.ServicioLogin = ServicioLogin;
        this.servicioEgreso = servicioEgreso;
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

        Usuario usuarioBuscado = ServicioLogin.consultarUsuario(datosLogin.getUsername(), datosLogin.getPassword());
        if (usuarioBuscado != null) {
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            return new ModelAndView("redirect:/index");
        } else {
            model.put("error", "Usuario o clave incorrecta");
        }
        return new ModelAndView("login", model);
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario) {
        ModelMap model = new ModelMap();
        try{
            ServicioLogin.registrar(usuario);
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
    public ModelAndView irAHome() {
        ModelAndView modelAndView = new ModelAndView("index");
        List<Egreso> egresos = servicioEgreso.getAllEgresos();
        modelAndView.addObject("egresos", egresos);
        return modelAndView;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}

