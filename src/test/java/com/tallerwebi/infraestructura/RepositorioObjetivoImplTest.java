package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.interfaces.RepositorioObjetivo;
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
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioObjetivoImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioObjetivo repositorioObjetivo;

    @BeforeEach
    public void init() {
        this.repositorioObjetivo = new RepositorioObjetivoImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void cuandoCreoUnObjetivoConDetallesEspecificosPuedoRecuperarloCorrectamente() {
        // Preparar
        Objetivo objetivo = new Objetivo();
        objetivo.setMontoObjetivo(75000.0);
        objetivo.setMontoActual(25000.0);
        objetivo.setNombre("Casa Nueva");

        // Ejecutar
        this.repositorioObjetivo.crearObjetivo(objetivo);
        sessionFactory.getCurrentSession().flush();

        // Verificar
        String hql = "SELECT o FROM Objetivo o WHERE o.nombre = :nombre";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Casa Nueva");

        Objetivo objetivoRecuperado = (Objetivo) query.getSingleResult();
        assertThat(objetivoRecuperado, notNullValue());
        assertThat(objetivoRecuperado.getMontoObjetivo(), equalTo(75000.0));
        assertThat(objetivoRecuperado.getMontoActual(), equalTo(25000.0));
    }

    @Test
    @Transactional
    @Rollback
    public void cuandoActualizoUnObjetivoDeberiaModificarseCorrectamente() {
        // Preparar
        Objetivo objetivo = new Objetivo();
        objetivo.setMontoObjetivo(100000.0);
        objetivo.setMontoActual(0.0);
        objetivo.setNombre("Viaje");

        // Crear el objetivo inicial
        this.repositorioObjetivo.crearObjetivo(objetivo);
        sessionFactory.getCurrentSession().flush();

        // Actualizar
        objetivo.setMontoActual(50000.0);
        this.repositorioObjetivo.guardar(objetivo);
        sessionFactory.getCurrentSession().flush();

        // Verificar
        Objetivo objetivoActualizado = this.repositorioObjetivo.buscarObjetivo(objetivo.getId());
        assertThat(objetivoActualizado.getMontoActual(), equalTo(50000.0));
    }

    @Test
    @Transactional
    @Rollback
    public void cuandoEliminoUnObjetivoDeberiaDesaparecerDelRepositorio() {
        // Preparar
        Objetivo objetivo = new Objetivo();
        objetivo.setMontoObjetivo(50000.0);
        objetivo.setMontoActual(0.0);
        objetivo.setNombre("Auto");

        // Crear objetivo
        this.repositorioObjetivo.crearObjetivo(objetivo);
        sessionFactory.getCurrentSession().flush();

        // Eliminar objetivo
        this.repositorioObjetivo.eliminarObjetivo(objetivo.getId());
        sessionFactory.getCurrentSession().flush();

        // Verificar que el objetivo fue eliminado
        List<Objetivo> objetivosRestantes = this.repositorioObjetivo.obtenerTodosLosObjetivos();
        assertThat(objetivosRestantes.size(), equalTo(0));

        // Verificar que el objetivo ya no se puede encontrar por su ID
        Objetivo objetivoBuscado = this.repositorioObjetivo.buscarObjetivo(objetivo.getId());
        assertThat(objetivoBuscado, equalTo(null));
    }

    @Test
    @Transactional
    @Rollback
    public void puedoAgregarVariosObjetivosYRecuperarlos() {
        // Preparar
        Objetivo objetivo1 = new Objetivo();
        objetivo1.setMontoObjetivo(100000.0);
        objetivo1.setMontoActual(0.0);
        objetivo1.setNombre("Viaje");

        Objetivo objetivo2 = new Objetivo();
        objetivo2.setMontoObjetivo(50000.0);
        objetivo2.setMontoActual(0.0);
        objetivo2.setNombre("Moto");

        // Crear objetivos
        this.repositorioObjetivo.crearObjetivo(objetivo1);
        this.repositorioObjetivo.crearObjetivo(objetivo2);
        sessionFactory.getCurrentSession().flush();

        // Verificar
        List<Objetivo> objetivosRecuperados = this.repositorioObjetivo.obtenerTodosLosObjetivos();
        assertThat(objetivosRecuperados.size(), equalTo(2));
        assertThat(objetivosRecuperados.get(0).getNombre(), equalTo("Viaje"));
        assertThat(objetivosRecuperados.get(1).getNombre(), equalTo("Moto"));
    }

    @Test
    @Transactional
    @Rollback
    public void puedoBuscarUnObjetivoEspecificoPorSuId() {
        // Preparar
        Objetivo objetivo = new Objetivo();
        objetivo.setMontoObjetivo(75000.0);
        objetivo.setMontoActual(25000.0);
        objetivo.setNombre("Computadora");

        // Crear objetivo
        this.repositorioObjetivo.crearObjetivo(objetivo);
        sessionFactory.getCurrentSession().flush();

        // Buscar por ID
        Objetivo objetoEncontrado = this.repositorioObjetivo.buscarObjetivo(objetivo.getId());

        // Verificar
        assertThat(objetoEncontrado, notNullValue());
        assertThat(objetoEncontrado.getNombre(), equalTo("Computadora"));
        assertThat(objetoEncontrado.getMontoObjetivo(), equalTo(75000.0));
    }
}