package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.interfaces.RepositorioTransaccion;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.ServicioTransaccion;
import com.tallerwebi.dominio.models.Transaccion;
import com.tallerwebi.dominio.models.Usuario;

import java.util.List;

public class ServicioTransaccionImpl implements ServicioTransaccion {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioTransaccion repositorioTransaccion;

    public ServicioTransaccionImpl(RepositorioUsuario repositorioUsuario, RepositorioTransaccion repositorioTransaccion) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioTransaccion = repositorioTransaccion;
    }

    @Override
    public Transaccion consultarTransaccion(Double monto, Integer id) throws RecursoNoEncontrado {
        Transaccion resultado = repositorioTransaccion.buscar(monto, id);
        if (resultado == null || resultado.getMonto() == null || resultado.getMonto() < 0.0
                || resultado.getId() == null || resultado.getId() < 0) {
            throw new RecursoNoEncontrado("Egreso no encontrado con monto: " + monto + " e id: " + id);
        }
        return resultado;
    }

    @Override
    public void crearTransaccion(Transaccion transaccion, Long userId) {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);
        if (usuario != null || transaccion.getId() != null || transaccion.getMonto() != null || userId != null) {
            repositorioTransaccion.guardar(transaccion);
            Double saldoActual = usuario.getSaldo();

            if (saldoActual >= transaccion.getMonto()) {
                Double nuevoSaldo = saldoActual - transaccion.getMonto();
                usuario.setSaldo(nuevoSaldo);
                repositorioUsuario.modificar(usuario);
                repositorioTransaccion.guardar(transaccion);
            } else {
                throw new SaldoInsuficiente("Saldo insuficiente para realizar la transaccion.");
            }
        }
    }

    @Override
    public List<Transaccion> getAllTransacciones() {
        return repositorioTransaccion.obtener();
    }

    @Override
    public List<Transaccion> getTransaccionPorUserId(Long userId) {
        return repositorioTransaccion.buscarTransaccionPorUsuario(userId);
    }

    @Override
    public void registrarTransaccionSinActualizarSaldo(Transaccion transaccion) {
        this.repositorioTransaccion.guardar(transaccion);
    }
}
