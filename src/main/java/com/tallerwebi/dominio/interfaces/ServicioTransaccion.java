package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Transaccion;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ServicioTransaccion {

    Transaccion consultarTransaccion(Double monto, Integer id) throws RecursoNoEncontrado;
    void crearTransaccion(Transaccion transaccion, Long userId);
    List<Transaccion> getAllTransacciones();
    List<Transaccion> getTransaccionPorUserId(Long userId);
    void registrarTransaccionSinActualizarSaldo(Transaccion transaccion);
}
