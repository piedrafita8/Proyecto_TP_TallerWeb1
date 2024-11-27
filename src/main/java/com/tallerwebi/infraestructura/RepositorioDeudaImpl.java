package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.interfaces.RepositorioDeuda;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioDeudaImpl implements RepositorioDeuda {

    private final EntityManager entityManager;

    public RepositorioDeudaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void guardar(Deuda deuda) {
        if (deuda.getId() == null) {
            entityManager.persist(deuda);
        } else {
            entityManager.merge(deuda);
        }
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Deuda deuda = entityManager.find(Deuda.class, id);
        if (deuda != null) {
            entityManager.remove(deuda);
        }
    }

    @Override
    @Transactional
    public List<Deuda> obtenerDeudasPorUsuario(Usuario usuario, TipoDeuda tipoDeuda) {
    String query = "SELECT d FROM Deuda d WHERE d.usuario = :usuario AND d.tipoDeuda = :tipoDeuda";
    return entityManager.createQuery(query, Deuda.class)
            .setParameter("usuario", usuario)
            .setParameter("tipoDeuda", tipoDeuda)
            .getResultList();
    }


    @Override
    @Transactional
    public void marcarComoPagada(Long id) {
        Deuda deuda = entityManager.find(Deuda.class, id);
        if (deuda != null) {
            deuda.setPagado(true);
            entityManager.merge(deuda);
        }
    }

    @Override
    public Deuda obtenerPorId(Long id) {
       return entityManager.find(Deuda.class, id) ;
    }
}

/* */