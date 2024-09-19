package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Ingreso;
import com.tallerwebi.dominio.RepositorioIngreso;
import org.hibernate.SessionFactory;

public class RepositorioIngresoImpl implements RepositorioIngreso {

    private SessionFactory sessionFactory;

    @Override
    public Ingreso buscarMontoIngreso(Integer montoIngreso, Integer id) {
        return null;
    }

    @Override
    public void guardar(Ingreso ingreso) {
        sessionFactory.getCurrentSession().save(ingreso);
    }

    @Override
    public Ingreso buscar(Integer montoABuscar) {
        return null;
    }

    @Override
    public void modificar(Ingreso ingreso) {
        sessionFactory.getCurrentSession().update(ingreso);
    }
}
