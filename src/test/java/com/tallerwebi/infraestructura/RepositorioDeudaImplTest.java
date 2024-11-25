package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.models.Deuda;
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
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioDeudaImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioDeudaImpl repositorioDeuda;

    @BeforeEach
    public void init() {
        this.repositorioDeuda = new RepositorioDeudaImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnaDeudaCuandoGuardoUnaNuevaDeudaEntoncesLaEncuentroEnLaBaseDeDatos() {
        
        Deuda deuda = new Deuda("Juan", 500.0, LocalDate.of(2024, 11, 25), TipoDeuda.ME_DEBEN, 1L);
        repositorioDeuda.guardar(deuda);

        String hql = "SELECT d FROM Deuda d WHERE d.monto = :monto AND d.nombre = :nombre AND d.userId = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("monto", 500.0);
        query.setParameter("nombre", "Juan");
        query.setParameter("userId", 1L);

        Deuda deudaObtenida = (Deuda) query.getSingleResult();

        // Verificar 
        assertThat(deudaObtenida.getMonto(), equalTo(500.0));
        assertThat(deudaObtenida.getNombre(), equalTo("Juan"));
        assertThat(deudaObtenida.getUserId(), equalTo(1L));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExistenDeudasCuandoObtengoDeudasPorUsuarioEntoncesDevuelveSoloLasCorrespondientes() {
       
        Deuda deuda1 = new Deuda("Juan", 200.0, LocalDate.of(2024, 11, 25), TipoDeuda.ME_DEBEN, 1L);
        Deuda deuda2 = new Deuda("Carlos", 300.0, LocalDate.of(2024, 11, 26), TipoDeuda.DEBO, 1L);
        Deuda deuda3 = new Deuda("Ana", 400.0, LocalDate.of(2024, 11, 27), TipoDeuda.ME_DEBEN, 2L);

        repositorioDeuda.guardar(deuda1);
        repositorioDeuda.guardar(deuda2);
        repositorioDeuda.guardar(deuda3);

        List<Deuda> deudasDebo = repositorioDeuda.obtenerDeudasPorUsuario(1L, true);
        assertThat(deudasDebo, hasSize(1));
        assertThat(deudasDebo.get(0).getNombre(), equalTo("Carlos"));

        List<Deuda> deudasMeDeben = repositorioDeuda.obtenerDeudasPorUsuario(1L, false);
        assertThat(deudasMeDeben, hasSize(1));
        assertThat(deudasMeDeben.get(0).getNombre(), equalTo("Juan"));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnaDeudaCuandoLaMarcoComoPagadaEntoncesElEstadoSeActualiza() {
        
        Deuda deuda = new Deuda("Pedro", 150.0, LocalDate.of(2024, 11, 25), TipoDeuda.ME_DEBEN, 1L);
        repositorioDeuda.guardar(deuda);

        String hql = "SELECT d FROM Deuda d WHERE d.nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Pedro");
        Deuda deudaObtenida = (Deuda) query.getSingleResult();

        repositorioDeuda.marcarComoPagada(deudaObtenida.getId());

        // Verificar 
        Deuda deudaActualizada = sessionFactory.getCurrentSession().get(Deuda.class, deudaObtenida.getId());
        assertThat(deudaActualizada.isPagado(), is(true));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnaDeudaCuandoLaEliminoEntoncesYaNoEstaEnLaBaseDeDatos() {
    
        Deuda deuda = new Deuda("Maria", 250.0, LocalDate.of(2024, 11, 25), TipoDeuda.DEBO, 1L);
        repositorioDeuda.guardar(deuda);

        String hql = "SELECT d FROM Deuda d WHERE d.nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Maria");
        Deuda deudaObtenida = (Deuda) query.getSingleResult();

        repositorioDeuda.eliminar(deudaObtenida.getId());

        // Verificar 
        Deuda deudaEliminada = sessionFactory.getCurrentSession().get(Deuda.class, deudaObtenida.getId());
        assertThat(deudaEliminada, is(nullValue()));
    }
}
