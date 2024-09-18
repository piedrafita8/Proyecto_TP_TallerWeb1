package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;

public interface ServicioIngreso {

    Ingreso consultarIngreso(Integer monto, Integer id);
    void registrar(Ingreso ingreso) throws RecursoNoEncontrado;

}
