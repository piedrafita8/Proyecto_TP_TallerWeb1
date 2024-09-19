package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Egreso;
import com.tallerwebi.dominio.RepositorioEgreso;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class RepositorioEgresoImpl implements RepositorioEgreso {

    private SessionFactory sessionFactory;

    @Override
    public Egreso buscarMontoEgreso(Double montoEgreso, Integer id) {
        return null;
    }

    @Override
    public void guardar(Egreso egreso) {
        sessionFactory.getCurrentSession().save(egreso);
    }

    @Override
    public Egreso buscar(Integer montoABuscar) {
        return (Egreso) sessionFactory.getCurrentSession().createCriteria(Egreso.class)
                .add(Restrictions.eq("monto", montoABuscar))
                .uniqueResult();
    }

    @Override
    public void modificar(Egreso egreso) {
        sessionFactory.getCurrentSession().update(egreso);
    }
}
