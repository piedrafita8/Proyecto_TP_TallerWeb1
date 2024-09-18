package com.tallerwebi.dominio;

public interface RepositorioIngreso {

    Ingreso buscarMontoIngreso(Integer montoIngreso);
    void guardar(Ingreso ingreso);
    Ingreso buscar(Integer montoABuscar);
    void modificar(Ingreso ingreso);

}
