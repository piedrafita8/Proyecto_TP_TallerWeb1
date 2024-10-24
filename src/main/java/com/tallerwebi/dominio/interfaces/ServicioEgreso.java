package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;

import java.util.List;

public interface ServicioEgreso {

    Egreso consultarEgreso(Double monto, Integer id) throws RecursoNoEncontrado;
    void registrar(Egreso egreso) throws RecursoNoEncontrado;
    void crearEgreso(Egreso egreso);
    List<Egreso> getAllEgresos();

}

