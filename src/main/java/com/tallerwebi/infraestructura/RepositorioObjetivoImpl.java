package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaces.RepositorioObjetivo;
import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.models.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
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

        query.where(builder.equal(root.get("id"), id));

        return sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
    }

    @Override
    public void crearObjetivo(Objetivo objetivo) {
        sessionFactory.getCurrentSession().save(objetivo);
    }

    @Override
    public void guardar(Objetivo objetivo) {
        sessionFactory.getCurrentSession().saveOrUpdate(objetivo);
    }

    @Override
    public List<Objetivo> obtenerTodosLosObjetivosPorUsuario(Long userId) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Objetivo> query = builder.createQuery(Objetivo.class);
        Root<Objetivo> root = query.from(Objetivo.class);
        Join<Objetivo, Usuario> usuarioJoin = root.join("usuario");

        query.where(builder.equal(usuarioJoin.get("id"), userId));

        return sessionFactory.getCurrentSession().createQuery(query).list();
    }

    @Override
    public List<Objetivo> obtenerTodosLosObjetivos() {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Objetivo> query = builder.createQuery(Objetivo.class);
        Root<Objetivo> root = query.from(Objetivo.class);

        root.fetch("usuario", JoinType.LEFT);

        query.select(root);

        return sessionFactory.getCurrentSession().createQuery(query).list();
    }

    @Override
    public void eliminarObjetivo(Integer id) {
        Objetivo objetivo = sessionFactory.getCurrentSession().get(Objetivo.class, id);
        if(objetivo != null) {
            objetivo.getUsuario().removeObjetivo(objetivo);
            sessionFactory.getCurrentSession().delete(objetivo);
        }
    }
}