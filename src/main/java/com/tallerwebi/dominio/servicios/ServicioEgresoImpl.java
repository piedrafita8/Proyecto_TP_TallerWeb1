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

    private static RepositorioEgreso repositorioEgreso;

    @Autowired
    public ServicioEgresoImpl(RepositorioEgreso repositorioEgreso){
        ServicioEgresoImpl.repositorioEgreso = repositorioEgreso;
    }

    @Override
    public Egreso consultarEgreso(Double monto, Integer id) {
        return RepositorioEgreso.buscar(monto, id);
    }

    @Override
    public void registrar(Egreso egreso) throws RecursoNoEncontrado {
        Egreso recursoEncontrado = RepositorioEgreso.buscar(egreso.getMonto(), egreso.getId());
        if(recursoEncontrado == null){
            throw new RecursoNoEncontrado("El recurso no se encuentra");
        }
        RepositorioEgreso.guardar(egreso);
    }

    @Override
    public Egreso crearEgreso(Egreso egreso) {
        RepositorioEgreso.guardar(egreso);  // Guarda el nuevo egreso en la base de datos
        return egreso;
    }

    @Override
    public List<Egreso> getAllEgresos() {
        return RepositorioEgreso.obtener();
    }
}
