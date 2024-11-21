package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Egreso;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioEgreso {

    void eliminar(Egreso egreso);
    void guardar(Egreso egreso);
    Egreso buscar(Double montoABuscar, Integer idABuscar);
    void modificar(Egreso egreso);
    List<Egreso> obtener();
    void actualizar(Egreso egreso);
    List<Egreso> buscarEgresosPorUsuario(Long userId);
}
