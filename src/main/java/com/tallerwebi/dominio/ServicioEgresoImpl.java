package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioEgreso")
@Transactional
public class ServicioEgresoImpl implements ServicioEgreso {

    private final RepositorioEgreso repositorioEgreso;

    @Autowired
    public ServicioEgresoImpl(RepositorioEgreso repositorioEgreso){
        this.repositorioEgreso = repositorioEgreso;
    }

    @Override
    public Egreso consultarEgreso(Double monto, Integer id) {
        return repositorioEgreso.buscar(monto, id);
    }

    @Override
    public void registrar(Egreso egreso) throws RecursoNoEncontrado {
        Egreso recursoEncontrado = repositorioEgreso.buscar(egreso.getMonto(), egreso.getId());
        if(recursoEncontrado == null){
            throw new RecursoNoEncontrado("El recurso no se encuentra");
        }
        repositorioEgreso.guardar(egreso);
    }

    @Override
    public Egreso crearEgreso(Egreso egreso) {
        repositorioEgreso.guardar(egreso);  // Guarda el nuevo egreso en la base de datos
        return egreso;
    }

    @Override
    public List<Egreso> getAllEgresos() {
        return repositorioEgreso.obtener();
    }
}
