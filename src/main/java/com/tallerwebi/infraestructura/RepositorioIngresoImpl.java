package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Egreso;
import com.tallerwebi.dominio.Ingreso;
import com.tallerwebi.dominio.RepositorioIngreso;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class RepositorioIngresoImpl implements RepositorioIngreso {

    private SessionFactory sessionFactory;

    public RepositorioIngresoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void eliminar(Ingreso ingreso) {
        sessionFactory.getCurrentSession().delete(ingreso);
    }

    @Override
    public void guardar(Ingreso ingreso) {
        sessionFactory.getCurrentSession().save(ingreso);
    }

    @Override
    public Ingreso buscar(Double montoABuscar, Integer idABuscar) {
        return (Ingreso) sessionFactory.getCurrentSession().createCriteria(Ingreso.class)
                .add(Restrictions.eq("monto", montoABuscar))
                .add(Restrictions.eq("id", idABuscar))
                .uniqueResult();
    }

    @Override
    public void modificar(Ingreso ingreso) {
        sessionFactory.getCurrentSession().update(ingreso);
    }

    @Override
    public List<Ingreso> obtener() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Ingreso", Ingreso.class)
                .list();
    }

    @Override
    public void actualizar(Ingreso ingreso) {
        sessionFactory.getCurrentSession().merge(ingreso);
    }

}
