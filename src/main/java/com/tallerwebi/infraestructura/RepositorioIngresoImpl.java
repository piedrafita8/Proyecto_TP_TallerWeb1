package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.interfaces.RepositorioIngreso;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
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
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Ingreso> query = builder.createQuery(Ingreso.class);
        Root<Ingreso> root = query.from(Ingreso.class);

        // Crear las condiciones de búsqueda
        Predicate montoPredicate = builder.equal(root.get("monto"), montoABuscar);
        Predicate idPredicate = builder.equal(root.get("id"), idABuscar);

        // Unir las condiciones
        query.where(builder.and(montoPredicate, idPredicate));

        return (Ingreso) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
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
