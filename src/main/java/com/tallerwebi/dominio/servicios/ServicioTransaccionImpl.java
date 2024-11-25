package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.enums.TipoMovimiento;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioTransaccion;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.ServicioTransaccion;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.models.Transaccion;
import com.tallerwebi.dominio.models.Usuario;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
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
    public void crearTransaccion(Transaccion transaccion, Long userId) throws UsuarioNoEncontrado {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);

        // Verificaciones
        if (usuario == null) {
            throw new UsuarioNoEncontrado("Usuario no encontrado con ID: " + userId);
        }
        if (transaccion.getMonto() == null || transaccion.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto de la transacción debe ser mayor a 0.");
        }

        // Actualizar saldo según tipo de movimiento
        Double saldoActual = usuario.getSaldo();

        if (transaccion instanceof Ingreso) {
            Double nuevoSaldo = saldoActual + transaccion.getMonto();
            usuario.setSaldo(nuevoSaldo);

        } else if (transaccion instanceof Egreso) {
            if (saldoActual >= transaccion.getMonto()) {
                Double nuevoSaldo = saldoActual - transaccion.getMonto();
                usuario.setSaldo(nuevoSaldo);
            } else {
                throw new SaldoInsuficiente("Saldo insuficiente para realizar la transacción.");
            }
        } else {
            throw new IllegalArgumentException("El tipo de transacción no es válido.");
        }

        repositorioUsuario.modificar(usuario);
        repositorioTransaccion.guardar(transaccion);
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
        this.repositorioTransaccion.guardar(transaccion);//Para que?
    }
}
