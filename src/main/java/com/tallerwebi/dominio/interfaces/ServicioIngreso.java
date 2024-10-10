package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;

import java.time.LocalDate;
import java.util.List;

public interface ServicioIngreso {

    Ingreso consultarIngreso(Double monto, Integer id) throws RecursoNoEncontrado;
    void registrar(Ingreso ingreso) throws RecursoNoEncontrado;
    List<Ingreso> getAllIngresos();
    Ingreso crearIngreso(Ingreso ingreso);
}
