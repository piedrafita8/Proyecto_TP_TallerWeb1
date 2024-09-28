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

    private final RepositorioIngreso RepositorioIngreso;

    @Autowired
    public ServicioIngresoImpl(RepositorioIngreso RepositorioIngreso) {
        this.RepositorioIngreso = RepositorioIngreso;
    }

    @Override
    public Ingreso consultarIngreso(Double monto, Integer id) {
        return RepositorioIngreso.buscar(monto, id);
    }

    @Override
    public void registrar(Ingreso ingreso) throws RecursoNoEncontrado {
        Ingreso recursoEncontrado = RepositorioIngreso.buscar(ingreso.getMonto(), ingreso.getId());
        if(recursoEncontrado == null){
            throw new RecursoNoEncontrado("El recurso no se encuentra");
        }
        RepositorioIngreso.guardar(ingreso);
    }

    @Override
    public List<Ingreso> getAllIngresos() {
        return RepositorioIngreso.obtener();
    }

    @Override
    public Ingreso crearIngreso(Ingreso ingreso) {
        RepositorioIngreso.guardar(ingreso);  // Guarda el nuevo egreso en la base de datos
        return ingreso;
    }
}
