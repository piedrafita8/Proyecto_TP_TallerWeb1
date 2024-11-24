package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaces.RepositorioTransaccion;
import com.tallerwebi.dominio.models.Transaccion;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class RepositorioTransaccionImpl implements RepositorioTransaccion {

    private SessionFactory sessionFactory;

    public RepositorioTransaccionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void eliminar(Transaccion transaccion) {
        sessionFactory.getCurrentSession().delete(transaccion);
    }

    @Override
    public void guardar(Transaccion transaccion) {
        sessionFactory.getCurrentSession().save(transaccion);
    }

    @Override
    public void actualizar(Transaccion transaccion) {
        sessionFactory.getCurrentSession().update(transaccion);
    }

    @Override
    public void modificar(Transaccion transaccion) {
        sessionFactory.getCurrentSession().merge(transaccion);
    }

    @Override
    public List<Transaccion> obtener() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Transaccion ", Transaccion.class)
                .list();
    }

    @Override
    public List<Transaccion> buscarTransaccionPorUsuario(Long userId) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Transaccion> query = builder.createQuery(Transaccion.class);
        Root<Transaccion> root = query.from(Transaccion.class);

        Predicate userIdPredicate = builder.equal(root.get("userId"), userId);
        query.where(userIdPredicate);

        return sessionFactory.getCurrentSession().createQuery(query).list();
    }

    @Override
    public Transaccion buscar(Double montoABuscar, Integer idABuscar) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Transaccion> query = builder.createQuery(Transaccion.class);
        Root<Transaccion> root = query.from(Transaccion.class);

        // Crear las condiciones de búsqueda
        Predicate montoPredicate = builder.equal(root.get("monto"), montoABuscar);
        Predicate idPredicate = builder.equal(root.get("id"), idABuscar);

        // Unir las condiciones
        query.where(builder.and(montoPredicate, idPredicate));

        return (Transaccion) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
    }
}
