package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Ingreso;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioIngreso {

    void eliminar(Ingreso ingreso);
    void guardar(Ingreso ingreso);
    Ingreso buscar(Double montoABuscar, Integer idABuscar);
    void modificar(Ingreso ingreso);
    List<Ingreso> obtener();
    void actualizar(Ingreso ingreso);
}
