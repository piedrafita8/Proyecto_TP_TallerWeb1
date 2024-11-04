package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioIngreso;

import com.tallerwebi.dominio.models.Usuario;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServicioIngresoImpl implements ServicioIngreso {


    private final RepositorioIngreso repositorioIngreso;
    private final RepositorioUsuario repositorioUsuario;

    public ServicioIngresoImpl(RepositorioIngreso repositorioIngreso, RepositorioUsuario repositorioUsuario) {
        this.repositorioIngreso = repositorioIngreso;
        this.repositorioUsuario = repositorioUsuario;
    }


    @Override
    public Ingreso consultarIngreso(Double monto, Integer id) throws RecursoNoEncontrado {
        // Aquí debes llamar al método correcto en la instancia inyectada
        Ingreso resultado = repositorioIngreso.buscar(monto, id);
        if (resultado == null) {
            throw new RecursoNoEncontrado("Ingreso no encontrado con monto: " + monto + " e id: " + id);
        }
        return resultado;
    }

    @Override
    public void registrar(Ingreso ingreso) throws RecursoNoEncontrado {

    }


    @Override
    public List<Ingreso> getAllIngresos() {
        return repositorioIngreso.obtener();
    }

    @Override
    public void crearIngreso(Ingreso ingreso, Long userId) {
        // Guardar el ingreso
        repositorioIngreso.guardar(ingreso);

        // Obtener el usuario por userId y actualizar su saldo
        Usuario usuario = repositorioUsuario.buscarPorId(userId);
        if (usuario != null) {
            Double nuevoSaldo = usuario.getSaldo() + ingreso.getMonto();
            usuario.setSaldo(nuevoSaldo);
            repositorioUsuario.modificar(usuario); // Guardar el nuevo saldo en la base de datos
        }
    }


}
