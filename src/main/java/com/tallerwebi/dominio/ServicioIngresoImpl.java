package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioIngreso")
@Transactional
public class ServicioIngresoImpl implements ServicioIngreso{

    private final RepositorioIngreso repositorioIngreso;

    @Autowired
    public ServicioIngresoImpl(RepositorioIngreso repositorioIngreso) {
        this.repositorioIngreso = repositorioIngreso;
    }

    @Override
    public Ingreso consultarIngreso(Double monto, Integer id) {
        return repositorioIngreso.buscarMontoIngreso(monto, id);
    }

    @Override
    public void registrar(Ingreso ingreso) throws RecursoNoEncontrado {
        Ingreso recursoEncontrado = repositorioIngreso.buscarMontoIngreso(ingreso.getMonto(), ingreso.getId());
        if(recursoEncontrado == null){
            throw new RecursoNoEncontrado("El recurso no se encuentra");
        }
        repositorioIngreso.guardar(ingreso);
    }
}
