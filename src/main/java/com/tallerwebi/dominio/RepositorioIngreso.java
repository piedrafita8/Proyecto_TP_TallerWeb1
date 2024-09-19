package com.tallerwebi.dominio;

public interface RepositorioIngreso {

    Ingreso buscarMontoIngreso(Double montoIngreso, Integer id);
    void guardar(Ingreso ingreso);
    Ingreso buscar(Double montoABuscar);
    void modificar(Ingreso ingreso);

}
