package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ServicioIngreso {

    Ingreso consultarIngreso(Double monto, Integer fecha);
    Ingreso registrarIngreso(Ingreso ingreso) throws RecursoNoEncontrado;
    Ingreso crearIngreso(Ingreso ingreso);
    List<Ingreso> getAllIngresos();

}
