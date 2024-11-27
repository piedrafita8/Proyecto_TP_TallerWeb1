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

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service("servicioTransaccion")
@Transactional
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
    @Transactional
    public void crearTransaccion(Transaccion transaccion, Long userId) throws SaldoInsuficiente {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);

        // Validar que el usuario y la transacción sean válidos
        if (usuario == null || transaccion == null || transaccion.getMonto() == null || transaccion.getMonto() <= 0) {
            throw new IllegalArgumentException("Usuario o transacción inválidos");
        }

        // Procesar ingreso
        if (transaccion instanceof Ingreso) {
            Double nuevoSaldo = usuario.getSaldo() + transaccion.getMonto();
            usuario.addTransaccion(transaccion);
            usuario.setSaldo(nuevoSaldo);

            // Guardar el ingreso y actualizar el saldo del usuario
            repositorioTransaccion.guardar(transaccion);
            repositorioUsuario.modificar(usuario);
        }
        // Procesar egreso
        else if (transaccion instanceof Egreso) {
            Double saldoActual = usuario.getSaldo();
            if (saldoActual >= transaccion.getMonto()) {
                Double nuevoSaldo = saldoActual - transaccion.getMonto(); // Restar del saldo
                usuario.addTransaccion(transaccion);
                usuario.setSaldo(nuevoSaldo);

                // Guardar el egreso y actualizar el saldo del usuario
                repositorioTransaccion.guardar(transaccion);
                repositorioUsuario.modificar(usuario);
            } else {
                throw new SaldoInsuficiente("Saldo insuficiente para realizar la transacción");
            }
        } else {
            throw new IllegalArgumentException("Tipo de transacción desconocido");
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
    public List<Transaccion> obtenerTodasLasTransaccionesPorUserId(Long userId) {
        return repositorioTransaccion.obtenerTodasLasTransaccionesPorUserId(userId);
    }

    @Override
    public void registrarTransaccionSinActualizarSaldo(Transaccion transaccion) {
        this.repositorioTransaccion.guardar(transaccion);//Para que?
    }
}
