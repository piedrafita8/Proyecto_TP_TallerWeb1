package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Ingreso;
import com.tallerwebi.dominio.RepositorioIngreso;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioIngresoImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioIngreso repositorioIngreso;

    @BeforeEach
    public void init(){
        this.repositorioIngreso = new RepositorioIngresoImpl(sessionFactory);
    }

    @Test
    @Transactional
    public void dadoQueExisteUnRepositorioIngresoCuandoAgregoUnIngresoConMonto130000EntoncesLoEncuentroEnLaBaseDeDatos(){
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(130000.0);
        this.sessionFactory.getCurrentSession().save(ingreso);
        this.repositorioIngreso.guardar(ingreso);

        String hql = "SELECT i FROM Ingreso i INNER JOIN i.monto WHERE i.monto = :monto";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("monto", 130000.0);
        Ingreso ingresoObtenido = (Ingreso)query.getSingleResult();

        assertThat(ingresoObtenido, equalTo(ingreso));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoGuardo3IngresosEntoncesEncuentro3IngresosEnLaBaseDeDatos(){
        Ingreso ingreso1 = new Ingreso();
        ingreso1.setMonto(130000.0);
        Ingreso ingreso2 = new Ingreso();
        ingreso2.setMonto(80000.0);
        Ingreso ingreso3 = new Ingreso();
        ingreso3.setMonto(199000.0);
        this.sessionFactory.getCurrentSession().save(ingreso1);
        this.sessionFactory.getCurrentSession().save(ingreso2);
        this.sessionFactory.getCurrentSession().save(ingreso3);

        List<Ingreso> ingresosObtenidos = this.repositorioIngreso.obtener();

        int cantidadEsperada = 3;
        assertThat(ingresosObtenidos.size(), equalTo(cantidadEsperada));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoActualizoUnIngresoEntoncesLoEncuentroEnLaBaseDeDatos(){
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(130000.0);
        this.sessionFactory.getCurrentSession().save(ingreso);
        Double nuevoMonto = 178000.0;
        ingreso.setMonto(nuevoMonto);

        this.repositorioIngreso.actualizar(ingreso);

       Query query = this.sessionFactory.getCurrentSession().createQuery("FROM Ingreso WHERE monto = :monto");
       query.setParameter("monto", 178000.0);

       Ingreso ingresoObtenido = (Ingreso)query.getSingleResult();

        assertThat(ingresoObtenido.getDescripcion(), equalTo(nuevoMonto));
    }
}
