package com.tallerwebi.infraestructura;

//import com.tallerwebi.dominio.models.Ingreso;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

//import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioObjetivoImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioObjetivo repositorioObjetivo;

    @BeforeEach
    public void init(){
        this.repositorioObjetivo = new RepositorioObjetivoImpl(sessionFactory);
    }

    @Test
    @Transactional
    public void dadoQueExisteUnRepositorioObjetivoCuandoCreoUnObjetivoConMonto50000EntoncesLoEncuentroEnLaBaseDeDatos(){
        // Crear un objeto Objetivo con el monto deseado
        Objetivo objetivo = new Objetivo();
        objetivo.setMontoObjetivo(50000.0);
        objetivo.setMontoActual(0.0);
        objetivo.setNombre("Nuevo Carro");

        // Guardar usando el repositorio
        this.repositorioObjetivo.crearObjetivo(objetivo);

        // Hacer la consulta HQL para encontrar el objetivo guardado
        String hql = "SELECT o FROM Objetivo o WHERE o.montoObjetivo = :montoObjetivo";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("montoObjetivo", 50000.0);

        // Obtener el resultado de la consulta
        Objetivo objetivoObtenido = (Objetivo) query.getSingleResult();

        // Verificar que el objetivo guardado es el mismo que el obtenido
        assertThat(objetivoObtenido, equalTo(objetivo));
    }

    @Test
    @Transactional
    public void dadoUnObjetivoExistenteCuandoActualizoElMontoActualEntoncesElMontoEsActualizadoCorrectamente() {
        // Crear un objetivo con monto objetivo y monto actual inicial
        Objetivo objetivo = new Objetivo();
        objetivo.setMontoObjetivo(10000.0);
        objetivo.setMontoActual(5000.0); // Monto inicial
        objetivo.setNombre("Vacaciones");

        // Guardar el objetivo en la base de datos
        this.repositorioObjetivo.crearObjetivo(objetivo);

        // Actualizar el monto actual del objetivo
        Double montoAAgregar = 2000.0;
        objetivo.setMontoActual(montoAAgregar);
        this.repositorioObjetivo.guardar(objetivo);

        // Hacer la consulta HQL para encontrar el objetivo actualizado
        String hql = "SELECT o FROM Objetivo o WHERE o.id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", objetivo.getId());

        // Obtener el objetivo de la base de datos
        Objetivo objetivoActualizado = (Objetivo) query.getSingleResult();

        // Verificar que el monto actual se haya actualizado correctamente
        assertThat(objetivoActualizado.getMontoActual(), equalTo(7000.0)); // 5000.0 + 2000.0
    }

    @Test
    @Transactional
    @Rollback
    public void dadoUnObjetivoExistentePuedoBorrarlo(){
        // Crear un objeto Objetivo con el monto deseado
        Objetivo objetivo = new Objetivo();
        objetivo.setMontoObjetivo(50000.0);
        objetivo.setMontoActual(0.0);
        objetivo.setNombre("Nuevo Carro");

        // Guardar usando el repositorio
        this.repositorioObjetivo.crearObjetivo(objetivo);

        //Borrarlo
        this.repositorioObjetivo.eliminarObjetivo(objetivo.getId());

        List<Objetivo> objetivoObtenidos = this.repositorioObjetivo.obtenerTodosLosObjetivos();
        Integer cantidadEsperada = 0;
        

        //Verificar que el objeto se elimino correctamente del repositorio
        assertThat(objetivoObtenidos.size(), equalTo(cantidadEsperada));
    }

    @Test
    @Transactional
    @Rollback
    public void dadosTresObjetivosExistentesPuedoConfirmarQueSeGuardaronEnLaBaseDeDatos(){
        // Crear un objeto Objetivo con el monto deseado
        Objetivo objetivo1 = new Objetivo();
        objetivo1.setMontoObjetivo(5000000.0);
        objetivo1.setMontoActual(0.0);
        objetivo1.setNombre("Nuevo Carro");

        Objetivo objetivo2 = new Objetivo();
        objetivo2.setMontoObjetivo(100000.0);
        objetivo2.setMontoActual(0.0);
        objetivo2.setNombre("Nuevo telefono");

        Objetivo objetivo3 = new Objetivo();
        objetivo3.setMontoObjetivo(800000.0);
        objetivo3.setMontoActual(0.0);
        objetivo3.setNombre("Nuevo ropero");

        // Guardar usando el repositorio
        this.repositorioObjetivo.crearObjetivo(objetivo1);
        this.repositorioObjetivo.crearObjetivo(objetivo2);
        this.repositorioObjetivo.crearObjetivo(objetivo3);

        List<Objetivo> objetivoObtenidos = this.repositorioObjetivo.obtenerTodosLosObjetivos();
        Integer cantidadEsperada = 3;

        assertThat(objetivoObtenidos.size(), equalTo(cantidadEsperada));
        assertThat(objetivoObtenidos.get(0),equalTo(objetivo1));
        assertThat(objetivoObtenidos.get(1),equalTo(objetivo2));
        assertThat(objetivoObtenidos.get(2),equalTo(objetivo3));

    }


    @Test
    @Transactional
    @Rollback
    public void dadoUnObjetivoExistentePuedoBuscarloYObtenerloCorrectamente(){
        // Crear un objeto Objetivo con el monto deseado
        Objetivo objetivo = new Objetivo();
        objetivo.setMontoObjetivo(50000.0);
        objetivo.setMontoActual(0.0);
        objetivo.setNombre("Nuevo Carro");

        // Guardar usando el repositorio
        this.repositorioObjetivo.crearObjetivo(objetivo);

        sessionFactory.getCurrentSession().flush();
        Integer idGuardado=objetivo.getId();

        //Buscar egreso y verificar
        Objetivo egresObtenido=this.repositorioObjetivo.buscarObjetivo(idGuardado);
        assertThat(egresObtenido,equalTo(objetivo));

    }

    
}