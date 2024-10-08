package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioEgreso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioEgreso")
@Transactional
public class ServicioEgresoImpl implements ServicioEgreso {

    private RepositorioEgreso repositorioEgreso;  // Eliminar el uso de static

    @Autowired
    public ServicioEgresoImpl(RepositorioEgreso repositorioEgreso){
        this.repositorioEgreso = repositorioEgreso;  // Usar 'this' para la inyección de dependencia
    }

    @Override
    public Egreso consultarEgreso(Double monto, Integer id) {
        return repositorioEgreso.buscar(monto, id);  // Llamar al metodo usando la instancia repositorioEgreso
    }

    @Override
    public Egreso registrarEgreso(Egreso egreso) throws RecursoNoEncontrado {
        Egreso recursoEncontrado = repositorioEgreso.buscar(egreso.getMonto(), egreso.getId());  // Llamar a través de la instancia
        if(recursoEncontrado == null){
            throw new RecursoNoEncontrado("El recurso no se encuentra");
        }
        repositorioEgreso.guardar(egreso);
        return recursoEncontrado;
    }

    @Override
    public Egreso crearEgreso(Egreso egreso) {
        repositorioEgreso.guardar(egreso);
        return egreso;
    }

    @Override
    public List<Egreso> getAllEgresos() {
        return repositorioEgreso.obtener();
    }
}
