package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.interfaces.ServicioObjetivo;
import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import com.tallerwebi.dominio.interfaces.RepositorioObjetivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioObjetivo")
@Transactional
public class ServicioObjetivoImpl implements ServicioObjetivo {

    private final RepositorioObjetivo repositorioObjetivo;

    @Autowired
    public ServicioObjetivoImpl(RepositorioObjetivo repositorioObjetivo) {
        this.repositorioObjetivo = repositorioObjetivo;
    }

    @Override
    public Objetivo consultarObjetivo(Integer id) {
        return repositorioObjetivo.buscarObjetivo(id);
    }

    @Override
    public void crearObjetivo(Objetivo objetivo) throws ObjetivoExistente {
        Objetivo objetivoExistente = repositorioObjetivo.buscarObjetivo(objetivo.getId());
        if (objetivoExistente != null) {
            throw new ObjetivoExistente("El objetivo ya existe");
        }
        repositorioObjetivo.crearObjetivo(objetivo);
    }

    @Override
    public void actualizarObjetivo(Integer id, Double montoAAgregar) {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(id);
        if (objetivo != null) {
            repositorioObjetivo.actualizarObjetivo(id, montoAAgregar);
        }
    }

    @Override
    public void eliminarObjetivo(Integer id) {
        Objetivo objetivo = repositorioObjetivo.buscarObjetivo(id);
        if (objetivo != null) {
            repositorioObjetivo.eliminarObjetivo(id);
        }
    }

    @Override
    public List<Objetivo> obtenerTodosLosObjetivos() {
        return repositorioObjetivo.obtenerTodosLosObjetivos();
    }

    @Override
    public List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long id) {
        return repositorioObjetivo.obtenerTodosLosObjetivosPorUsuario(id);
    }

}
