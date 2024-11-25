package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.interfaces.RepositorioDeuda;
import com.tallerwebi.dominio.models.Deuda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        session.saveOrUpdate(deuda);
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
    public List<Deuda> obtenerDeudasPorUsuario(Long userId, boolean tipoDeudaEsDebo) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Deuda> criteriaQuery = builder.createQuery(Deuda.class);
        Root<Deuda> root = criteriaQuery.from(Deuda.class);

        Predicate userIdPredicate = builder.equal(root.get("userId"), userId);
        Predicate tipoDeudaPredicate = builder.equal(root.get("tipoDeuda"), 
    tipoDeudaEsDebo ? TipoDeuda.DEBO : TipoDeuda.ME_DEBEN);
    
        criteriaQuery.where(builder.and(userIdPredicate, tipoDeudaPredicate));

        return session.createQuery(criteriaQuery).getResultList();
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
}
