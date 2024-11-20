package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaces.RepositorioObjetivo;
import com.tallerwebi.dominio.models.Objetivo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class RepositorioObjetivoImpl implements RepositorioObjetivo {

    private SessionFactory sessionFactory;

    public RepositorioObjetivoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Objetivo buscarObjetivo(Integer id) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Objetivo> query = builder.createQuery(Objetivo.class);
        Root<Objetivo> root = query.from(Objetivo.class);

        Predicate idPredicate = builder.equal(root.get("id"), id);

        query.where(builder.and(idPredicate));

        return sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
    }

    @Override
    public void crearObjetivo(Objetivo objetivo) {
        sessionFactory.getCurrentSession().save(objetivo);
    }

    @Override
    public void actualizarObjetivo(Integer id, Double montoAAgregar) {
        Objetivo objetivo = sessionFactory.getCurrentSession().get(Objetivo.class, id);
        if (objetivo != null) {
            objetivo.setMontoActual(objetivo.getMontoActual() + montoAAgregar);
            sessionFactory.getCurrentSession().update(objetivo);
        }
    }

    @Override
    public void guardar(Objetivo objetivo) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(objetivo);
    }

    public List<Objetivo> obtenerTodosLosObjetivos() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Objetivo", Objetivo.class)
                .list();
    }

    public List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long userId) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Objetivo> query = builder.createQuery(Objetivo.class);
        Root<Objetivo> root = query.from(Objetivo.class);

        Predicate userIdPredicate = builder.equal(root.get("userId"), userId);
        query.where(userIdPredicate);

        return sessionFactory.getCurrentSession().createQuery(query).list();
    }


    public void eliminarObjetivo(Integer id) {
        Objetivo objetivo = sessionFactory.getCurrentSession().get(Objetivo.class, id);
        if(objetivo != null) {
            sessionFactory.getCurrentSession().delete(objetivo);
        }
    }
}

