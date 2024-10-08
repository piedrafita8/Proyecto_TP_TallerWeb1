package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioIngreso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioIngreso")
@Transactional
public class ServicioIngresoImpl implements ServicioIngreso {

    private RepositorioIngreso repositorioIngreso;

    @Autowired
    public ServicioIngresoImpl(RepositorioIngreso repositorioIngreso) {
        this.repositorioIngreso = repositorioIngreso;
    }

    @Override
    public Ingreso consultarIngreso(Double monto, Integer id) {
        return repositorioIngreso.buscar(monto, id);
    }

    @Override
    public Ingreso registrarIngreso(Ingreso ingreso) throws RecursoNoEncontrado {
        Ingreso recursoEncontrado = repositorioIngreso.buscar(ingreso.getMonto(), ingreso.getId());
        if(recursoEncontrado == null){
            throw new RecursoNoEncontrado("El recurso no se encuentra");
        }
        repositorioIngreso.guardar(ingreso);
        return recursoEncontrado;
    }

    @Override
    public List<Ingreso> getAllIngresos() {
        return repositorioIngreso.obtener();
    }

    @Override
    public Ingreso crearIngreso(Ingreso ingreso) {
        repositorioIngreso.guardar(ingreso);  // Guarda el nuevo egreso en la base de datos
        return ingreso;
    }
}
