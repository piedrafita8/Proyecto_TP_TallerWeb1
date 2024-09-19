package com.tallerwebi.dominio;

public interface RepositorioEgreso {

    Egreso buscarMontoEgreso(Double montoEgreso, Integer id);
    void guardar(Egreso egreso);
    Egreso buscar(Double montoABuscar);
    void modificar(Egreso egreso);

}
