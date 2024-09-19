package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Egreso;
import com.tallerwebi.dominio.RepositorioEgreso;
import org.hibernate.SessionFactory;

public class RepositorioEgresoImpl implements RepositorioEgreso {

    private SessionFactory sessionFactory;

    @Override
    public Egreso buscarMontoEgreso(Integer montoEgreso, Integer id) {
        return null;
    }

    @Override
    public void guardar(Egreso egreso) {
        sessionFactory.getCurrentSession().save(egreso);
    }

    @Override
    public Egreso buscar(Integer montoABuscar) {
        return null;
    }

    @Override
    public void modificar(Egreso egreso) {
        sessionFactory.getCurrentSession().update(egreso);
    }
}
