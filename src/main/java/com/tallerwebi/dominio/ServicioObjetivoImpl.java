package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioObjetivo")
@Transactional
public class ServicioObjetivoImpl implements ServicioObjetivo{

    private final RepositorioObjetivo repositorioObjetivo;

    @Autowired
    public ServicioObjetivoImpl(RepositorioObjetivo repositorioObjetivo) {
        this.repositorioObjetivo = repositorioObjetivo;
    }

    @Override
    public Objetivo consultarObjetivo(Integer id, String nombre) {
        return repositorioObjetivo.buscarObjetivo(id, nombre);
    }

    @Override
    public void registrar(Objetivo objetivo) throws ObjetivoExistente {
        Objetivo objetivoEncontrado = repositorioObjetivo.buscarObjetivo(objetivo.getId(), objetivo.getNombre());
        if(objetivoEncontrado != null){
            throw new ObjetivoExistente("El objetivo ya existe");
        }
        repositorioObjetivo.guardar(objetivo);
    }
}
