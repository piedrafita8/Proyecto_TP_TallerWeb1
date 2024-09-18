package com.tallerwebi.dominio;

public interface RepositorioEgreso {

    Egreso buscarMontoEgreso(Integer montoEgreso);
    void guardar(Egreso egreso);
    Egreso buscar(Integer montoABuscar);
    void modificar(Egreso egreso);

}
