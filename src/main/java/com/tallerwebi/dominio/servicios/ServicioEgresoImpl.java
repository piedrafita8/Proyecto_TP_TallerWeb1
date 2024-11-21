package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioEgreso;
import com.tallerwebi.dominio.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServicioEgresoImpl implements ServicioEgreso {

    private final RepositorioEgreso repositorioEgreso;
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioEgresoImpl(RepositorioEgreso repositorioEgreso, RepositorioUsuario repositorioUsuario){
        this.repositorioEgreso = repositorioEgreso;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Egreso consultarEgreso(Double monto, Integer id) throws RecursoNoEncontrado {
        // Aquí debes llamar al metodo correcto en la instancia inyectada
        Egreso resultado = repositorioEgreso.buscar(monto, id);  // Usar repositorioEgreso en lugar de RepositorioEgresoImpl
        if (resultado == null) {
            throw new RecursoNoEncontrado("Egreso no encontrado con monto: " + monto + " e id: " + id);
        }
        return resultado;
    }

    @Override
    public void registrar(Egreso egreso) throws RecursoNoEncontrado {
        Egreso recursoEncontrado = repositorioEgreso.buscar(egreso.getMonto(), egreso.getId());  // Llamar a través de la instancia
        if(recursoEncontrado == null){
            throw new RecursoNoEncontrado("El recurso no se encuentra");
        }
        repositorioEgreso.guardar(egreso);
    }

    @Override
    public void crearEgreso(Egreso egreso, Long userId) {
        repositorioEgreso.guardar(egreso);

        Usuario usuario = repositorioUsuario.buscarPorId(userId);
        if (usuario != null) {
            Double saldoActual = usuario.getSaldo();

            if (saldoActual >= egreso.getMonto()) {
                Double nuevoSaldo = saldoActual - egreso.getMonto();
                usuario.setSaldo(nuevoSaldo);
                repositorioUsuario.modificar(usuario);
            } else {
                throw new SaldoInsuficiente("Saldo insuficiente para realizar el egreso.");
            }
        }
    }


    @Override
    public List<Egreso> getAllEgresos() {
        return repositorioEgreso.obtener();
    }
}
