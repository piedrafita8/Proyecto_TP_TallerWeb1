package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;

public interface ServicioIngreso {

    Ingreso consultarIngreso(Double monto, Integer id);
    void registrar(Ingreso ingreso) throws RecursoNoEncontrado;

}
