package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Egreso;
import com.tallerwebi.dominio.Objetivo;
import com.tallerwebi.dominio.RepositorioObjetivo;
import org.hibernate.SessionFactory;

public class RepositorioObjetivoImpl implements RepositorioObjetivo {

    private SessionFactory sessionFactory;

    @Override
    public Objetivo buscarObjetivo(Integer id, String nombre) {
        return null;
    }

    @Override
    public void guardar(Objetivo objetivo) {
        sessionFactory.getCurrentSession().save(objetivo);
    }

    @Override
    public Egreso buscar(Integer metaMonto) {
        return null;
    }

    @Override
    public void modificar(Objetivo objetivo) {
        sessionFactory.getCurrentSession().update(objetivo);
    }
}
