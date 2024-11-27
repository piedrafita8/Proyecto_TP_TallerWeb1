package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
/*
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioDeudaImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioDeudaImpl repositorioDeuda;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Configurar el repositorio con EntityManager basado en la sesión actual
        var session = sessionFactory.openSession();
        var entityManager = session.getEntityManagerFactory().createEntityManager();
        repositorioDeuda = new RepositorioDeudaImpl(entityManager);

        // Crear un usuario base para las pruebas
        session.beginTransaction();
        usuario = new Usuario("testuser", "testuser@example.com", "password", "ROLE_USER");
        session.save(usuario);
        session.getTransaction().commit();
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnaDeudaCuandoGuardoUnaNuevaDeudaEntoncesLaEncuentroEnLaBaseDeDatos() {
        // Crear una deuda de prueba
        Deuda deuda = new Deuda("Préstamo", 1000.0, LocalDate.now(), TipoDeuda.DEBO, "Juan", 1L);
        repositorioDeuda.guardar(deuda);

        // Recuperar la deuda desde la base de datos
        Deuda deudaGuardada = repositorioDeuda.obtenerPorId(deuda.getId());
        assertThat(deudaGuardada, notNullValue());
        assertThat(deudaGuardada.getDescripcion(), equalTo("Préstamo"));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExistenDeudasCuandoObtengoDeudasPorUsuarioEntoncesDevuelveSoloLasCorrespondientes() {
        // Crear dos deudas con diferentes tipos
        Deuda deuda1 = new Deuda("Préstamo", 1000.0, LocalDate.now(), TipoDeuda.DEBO, "Juan", 1L);
        Deuda deuda2 = new Deuda("Cobro", 500.0, LocalDate.now(), TipoDeuda.ME_DEBEN, "María", 1L);

        repositorioDeuda.guardar(deuda1);
        repositorioDeuda.guardar(deuda2);

        // Obtener las deudas del tipo PENDIENTE
        List<Deuda> deudasPendientes = repositorioDeuda.obtenerDeudasPorUsuario(usuario, TipoDeuda.DEBO);
        assertThat(deudasPendientes, hasSize(1));
        assertThat(deudasPendientes.get(0).getDescripcion(), equalTo("Préstamo"));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnaDeudaCuandoLaMarcoComoPagadaEntoncesElEstadoSeActualiza() {
        // Crear una deuda pendiente
        Deuda deuda = new Deuda("Préstamo", 1000.0, LocalDate.now(), TipoDeuda.DEBO, "Juan", 1L);
        repositorioDeuda.guardar(deuda);

        // Marcar la deuda como pagada
        repositorioDeuda.marcarComoPagada(deuda.getId());

        // Verificar el estado actualizado
        Deuda deudaActualizada = repositorioDeuda.obtenerPorId(deuda.getId());
        assertThat(deudaActualizada.isPagado(), is(true));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnaDeudaCuandoLaEliminoEntoncesYaNoEstaEnLaBaseDeDatos() {
        // Crear una deuda de prueba
        Deuda deuda = new Deuda("Préstamo", 1000.0, LocalDate.now(), TipoDeuda.DEBO, "Juan", 1L);
        repositorioDeuda.guardar(deuda);

        // Eliminar la deuda
        repositorioDeuda.eliminar(deuda.getId());

        // Verificar que ya no existe en la base de datos
        Deuda deudaEliminada = repositorioDeuda.obtenerPorId(deuda.getId());
        assertThat(deudaEliminada, nullValue());
    }
}*/