package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioEgreso {

    void eliminar(Egreso egreso);
    void guardar(Egreso egreso);
    Egreso buscar(Double montoABuscar, Integer idABuscar);
    void modificar(Egreso egreso);
    List<Egreso> obtener();
    void actualizar(Egreso egreso);
}
