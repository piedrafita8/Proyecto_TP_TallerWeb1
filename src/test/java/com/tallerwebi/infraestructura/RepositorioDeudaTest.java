package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)  
@ContextConfiguration(classes = HibernateInfraestructuraTestConfig.class)
@Import(HibernateInfraestructuraTestConfig.class)  
public class RepositorioDeudaTest {

    @Autowired
    private SessionFactory sessionFactory;

    private Session session;

    @BeforeEach
    public void setUp() {
        session = sessionFactory.openSession();
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarDeuda() {
        Usuario usuario = new Usuario("usuario1", "usuario1@example.com", "password", "USER");
        
        Deuda deuda = new Deuda("Deuda de prueba", 100.0, LocalDate.now(), TipoDeuda.DEBO, "Otra Persona", usuario);
        usuario.agregarDeudaPendiente(deuda);  
        session.beginTransaction();
        session.save(usuario);
        session.getTransaction().commit();

        assertNotNull(deuda.getId(), "La deuda no se guardó correctamente.");
        assertTrue(deuda.isPagado() == false, "La deuda no está correctamente marcada como no pagada.");
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerDeudaPorId() {
        Usuario usuario = new Usuario("usuario2", "usuario2@example.com", "password", "USER");
        Deuda deuda = new Deuda("Deuda de prueba", 200.0, LocalDate.now(), TipoDeuda.DEBO, "Otra Persona", usuario);
        usuario.agregarDeudaPendiente(deuda);

        session.beginTransaction();
        session.save(usuario);
        session.getTransaction().commit();

        Deuda deudaRecuperada = session.get(Deuda.class, deuda.getId());
        
        assertNotNull(deudaRecuperada, "La deuda no se recuperó correctamente.");
        assertTrue(deudaRecuperada.getMonto().equals(200.0), "El monto de la deuda no coincide.");
    }

    @Test
    @Transactional
    @Rollback
    public void testMarcarDeudaComoPagada() {
        
        Usuario usuario = new Usuario("usuario3", "usuario3@example.com", "password", "USER");
        Deuda deuda = new Deuda("Deuda de prueba", 300.0, LocalDate.now(), TipoDeuda.DEBO, "Otra Persona", usuario);
        usuario.agregarDeudaPendiente(deuda);

        session.beginTransaction();
        session.save(usuario);
        session.getTransaction().commit();

        deuda.setPagado(true);

        session.beginTransaction();
        session.update(deuda);
        session.getTransaction().commit();

        Deuda deudaRecuperada = session.get(Deuda.class, deuda.getId());
        assertTrue(deudaRecuperada.isPagado(), "La deuda no se ha marcado correctamente como pagada.");
    }
}