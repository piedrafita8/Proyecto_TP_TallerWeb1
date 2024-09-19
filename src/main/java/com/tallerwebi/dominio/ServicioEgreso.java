package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;

public interface ServicioEgreso {

    Egreso consultarEgreso(Double monto, Integer id);
    void registrar(Egreso egreso) throws RecursoNoEncontrado;

}
