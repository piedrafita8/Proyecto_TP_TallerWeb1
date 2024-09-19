package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        return repositorioEgreso.buscarMontoEgreso(monto, id);
    }

    @Override
    public void registrar(Egreso egreso) throws RecursoNoEncontrado {
        Egreso recursoEncontrado = repositorioEgreso.buscarMontoEgreso(egreso.getMonto(), egreso.getId());
        if(recursoEncontrado == null){
            throw new RecursoNoEncontrado("El recurso no se encuentra");
        }
        repositorioEgreso.guardar(egreso);
    }
}
