package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.models.Transaccion;

import java.util.List;

public interface RepositorioTransaccion {

    void eliminar(Transaccion transaccion);
    void guardar(Transaccion transaccion);
    void actualizar(Transaccion transaccion);
    void modificar(Transaccion transaccion);
    List<Transaccion> obtener();
    List<Transaccion> buscarTransaccionPorUsuario(Long userId);
    Transaccion buscar(Double montoABuscar, Integer idABuscar);
    public List<Transaccion> obtenerTodasLasTransaccionesPorUserId(Long userId);

}
