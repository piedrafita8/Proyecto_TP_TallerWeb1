package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Modelo;
import com.tallerwebi.dominio.RepositorioModelo;
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
    @Rollback
    public void dadoQueExisteUnRepositorioModeloCuandoGuardoUnModeloEntoncesLoEncuentroEnLaBaseDeDatos(){
        Modelo modelo = new Modelo();
        modelo.setDescripcion("Focus");

        this.repositorioModelo.guardar(modelo);

        String hql = "FROM Modelo WHERE descripcion = :descripcion";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("descripcion", "Focus");
        Modelo modeloObtenido = (Modelo)query.getSingleResult();

        assertThat(modeloObtenido, equalTo(modelo));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioModeloCuandoGuardo3ModelosEntoncesEncuentro3ModelosEnLaBaseDeDatos(){
        Modelo focus = new Modelo();
        focus.setDescripcion("Focus");
        Modelo gol = new Modelo();
        gol.setDescripcion("Gol");
        Modelo palio = new Modelo();
        palio.setDescripcion("Palio");
        this.sessionFactory.getCurrentSession().save(focus);
        this.sessionFactory.getCurrentSession().save(gol);
        this.sessionFactory.getCurrentSession().save(palio);

        List<Modelo> modelosObtenidos = this.repositorioModelo.obtener();

        int cantidadEsperada = 3;
        assertThat(modelosObtenidos.size(), equalTo(cantidadEsperada));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioModeloCuandoActualizoUnModeloEntoncesLoEncuentroEnLaBaseDeDatos(){
        Modelo modelo = new Modelo();
        modelo.setDescripcion("Focus");
        this.sessionFactory.getCurrentSession().save(modelo);
        String nuevaDescripcion = "Gol";
        modelo.setDescripcion(nuevaDescripcion);

        this.repositorioModelo.actualizar(modelo);

       Query query = this.sessionFactory.getCurrentSession().createQuery("FROM Modelo WHERE descripcion = :descripcion");
       query.setParameter("descripcion", "Gol");

       Modelo modeloObtenido = (Modelo)query.getSingleResult();

        assertThat(modeloObtenido.getDescripcion(), equalTo(nuevaDescripcion));
    }
}
