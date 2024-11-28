package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.interfaces.RepositorioDeuda;
import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class RepositorioDeudaImpl implements RepositorioDeuda {

    private final SessionFactory sessionFactory;

    public RepositorioDeudaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Deuda deuda) {
        Session session = sessionFactory.getCurrentSession();
        if (deuda.getId() == null) {
            session.save(deuda);
        } else {
            session.update(deuda);
        }
    }

    @Override
    public void eliminar(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Deuda deuda = session.get(Deuda.class, id);
        if (deuda != null) {
            session.delete(deuda);
        }
    }

    @Override
    public List<Deuda> obtenerDeudasPorUsuario(Usuario usuario, TipoDeuda tipoDeuda) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Deuda> query = builder.createQuery(Deuda.class);
        Root<Deuda> root = query.from(Deuda.class);

        // Construcción de la consulta con filtros
        Predicate usuarioPredicate = builder.equal(root.get("usuario"), usuario);
        Predicate tipoDeudaPredicate = builder.equal(root.get("tipoDeuda"), tipoDeuda);
        query.where(builder.and(usuarioPredicate, tipoDeudaPredicate));

        return session.createQuery(query).getResultList();
    }

    @Override
    public void marcarComoPagada(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Deuda deuda = session.get(Deuda.class, id);
        if (deuda != null) {
            deuda.setPagado(true);
            session.update(deuda);
        }
    }

    @Override
    public Deuda obtenerPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Deuda.class, id);
    }
}

/* */