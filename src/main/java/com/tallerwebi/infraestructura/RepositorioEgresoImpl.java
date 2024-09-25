package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Egreso;
import com.tallerwebi.dominio.Ingreso;
import com.tallerwebi.dominio.RepositorioEgreso;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class RepositorioEgresoImpl implements RepositorioEgreso {

    private SessionFactory sessionFactory;

    public RepositorioEgresoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void eliminar(Egreso egreso) {
        sessionFactory.getCurrentSession().delete(egreso);
    }

    @Override
    public void guardar(Egreso egreso) {
        sessionFactory.getCurrentSession().save(egreso);
    }

    @Override
    public Egreso buscar(Double montoABuscar, Integer idABuscar) {
        return (Egreso) sessionFactory.getCurrentSession().createCriteria(Egreso.class)
                .add(Restrictions.eq("monto", montoABuscar))
                .add(Restrictions.eq("id", idABuscar))
                .uniqueResult();
    }

    @Override
    public void modificar(Egreso egreso) {
        sessionFactory.getCurrentSession().update(egreso);
    }

    @Override
    public List<Egreso> obtener() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Egreso", Egreso.class)
                .list();
    }

    @Override
    public void actualizar(Egreso egreso) {
        sessionFactory.getCurrentSession().merge(egreso);
    }

}
