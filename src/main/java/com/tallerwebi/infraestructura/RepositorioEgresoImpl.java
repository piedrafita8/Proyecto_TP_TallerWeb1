package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.interfaces.RepositorioEgreso;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
@Repository
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
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Egreso> query = builder.createQuery(Egreso.class);
        Root<Egreso> root = query.from(Egreso.class);

        // Crear las condiciones de búsqueda
        Predicate montoPredicate = builder.equal(root.get("monto"), montoABuscar);
        Predicate idPredicate = builder.equal(root.get("id"), idABuscar);

        // Unir las condiciones
        query.where(builder.and(montoPredicate, idPredicate));

        return (Egreso) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
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
