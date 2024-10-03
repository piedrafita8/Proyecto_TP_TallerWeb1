package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorIngreso {

    private ServicioIngreso ingresoService;

    public ControladorIngreso(ServicioIngreso ingresoService) {
        this.ingresoService = ingresoService;
    }

    @GetMapping("/ingreso")
    public List<Ingreso> todosLosIngresos() {
        return ingresoService.getAllIngresos();
    }

    @PostMapping("/ingreso")
    public ResponseEntity<Ingreso> crearIngreso(@RequestBody Ingreso ingreso) {
        Ingreso nuevoIngreso = ingresoService.crearIngreso(ingreso);
        return new ResponseEntity<>(nuevoIngreso, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/validar-ingreso", method = RequestMethod.POST)
    public ModelAndView validarIngreso(@ModelAttribute("datosIngreso") DatosIngreso datosIngresoMock, HttpServletRequest requestMock) {
        ModelMap model = new ModelMap();

        Ingreso ingresoBuscado = ingresoService.consultarIngreso(datosIngresoMock.getMonto(), datosIngresoMock.getFecha());
        if (ingresoBuscado != null) {
            requestMock.getSession().setAttribute("Ingreso proveniente de mi sueldo", ingresoBuscado.getDescripcion());
            return new ModelAndView("redirect:/index");
        } else {
            model.put("error", "Ingreso o clave incorrecta");
        }
        return new ModelAndView("ingreso", model);
    }

    // Otros métodos como PUT, DELETE
}