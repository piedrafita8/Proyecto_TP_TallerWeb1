package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioIngreso;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServicioIngresoImpl implements ServicioIngreso {


    private final RepositorioIngreso repositorioIngreso;

    public ServicioIngresoImpl( RepositorioIngreso repositorioIngreso) {
        this.repositorioIngreso = repositorioIngreso;
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
    public void crearIngreso(Ingreso ingreso) {
        repositorioIngreso.guardar(ingreso);
    }


}
