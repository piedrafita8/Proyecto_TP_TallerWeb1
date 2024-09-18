package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Auto;
import com.tallerwebi.dominio.Modelo;
import com.tallerwebi.dominio.RepositorioAuto;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Query;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioEgresoImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioEgreso repositorioEgreso;

    @BeforeEach
    public void init(){
        this.repositorioEgreso = new RepositorioEgresoImpl(sessionFactory);
    }

    @Test
    @Transactional
    public void dadoQueExisteUnRepositorioEgresoCuandoIngresoUnGastoEntoncesLoEncuentroEnLaBaseDeDatos(){
        Egreso egreso = new Egreso();
        egreso.setDescripcion("Focus");
        this.sessionFactory.getCurrentSession().save(modelo);

        Auto auto = new Auto();

        auto.setModelo(modelo);

        this.repositorioEgreso.guardar(auto);

        String hql = "SELECT a FROM Auto a INNER JOIN a.modelo WHERE a.modelo.descripcion = :modelo";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("modelo", "Focus");
        Auto autoObtenido = (Auto)query.getSingleResult();

        assertThat(autoObtenido, equalTo(auto));
    }

}
