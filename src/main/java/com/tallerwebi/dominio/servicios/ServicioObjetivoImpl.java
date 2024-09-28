package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.interfaces.ServicioObjetivo;
import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.excepcion.ObjetivoExistente;
import com.tallerwebi.dominio.interfaces.RepositorioObjetivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioObjetivo")
@Transactional
public class ServicioObjetivoImpl implements ServicioObjetivo {

    private final RepositorioObjetivo RepositorioObjetivo;

    @Autowired
    public ServicioObjetivoImpl(RepositorioObjetivo RepositorioObjetivo) {
        this.RepositorioObjetivo = RepositorioObjetivo;
    }

    @Override
    public Objetivo consultarObjetivo(Integer id, String nombre) {
        return RepositorioObjetivo.buscarObjetivo(id, nombre);
    }

    @Override
    public void registrar(Objetivo objetivo) throws ObjetivoExistente {
        Objetivo objetivoEncontrado = RepositorioObjetivo.buscarObjetivo(objetivo.getId(), objetivo.getNombre());
        if(objetivoEncontrado != null){
            throw new ObjetivoExistente("El objetivo ya existe");
        }
        RepositorioObjetivo.guardar(objetivo);
    }
}
