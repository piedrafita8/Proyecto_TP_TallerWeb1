package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioIngreso {

    void eliminar(Ingreso ingreso);
    void guardar(Ingreso ingreso);
    Ingreso buscar(Double montoABuscar, Integer idABuscar);
    void modificar(Ingreso ingreso);
    List<Ingreso> obtener();
    void actualizar(Ingreso ingreso);
}
