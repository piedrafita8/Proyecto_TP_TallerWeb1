package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.models.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

@Override
@Transactional
public Usuario buscarUsuario(String email, String password) {
    return sessionFactory.getCurrentSession()
            .createQuery("from Usuario where email = :email and password = :password", Usuario.class)
            .setParameter("email", email)
            .setParameter("password", password)
            .uniqueResult();
}


@Transactional
@Override
public void guardar(Usuario usuario) {
    sessionFactory.getCurrentSession().save(usuario);
}



    @Override
    @Transactional
    public Usuario buscar(String email) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Usuario where email = :email", Usuario.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    @Override
    @Transactional
    public Usuario buscarPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Usuario.class, id);
    }

    @Override
    @Transactional
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }


    
}
