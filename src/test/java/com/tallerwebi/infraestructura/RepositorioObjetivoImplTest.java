package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.models.Objetivo;
import com.tallerwebi.dominio.interfaces.RepositorioObjetivo;
import com.tallerwebi.infraestructura.config.HibernateTestConfig;
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
@ContextConfiguration(classes = {HibernateTestConfig.class})
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
        this.repositorioObjetivo.actualizarObjetivo(objetivo.getId(), montoAAgregar);

        // Hacer la consulta HQL para encontrar el objetivo actualizado
        String hql = "SELECT o FROM Objetivo o WHERE o.id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", objetivo.getId());

        // Obtener el objetivo de la base de datos
        Objetivo objetivoActualizado = (Objetivo) query.getSingleResult();

        // Verificar que el monto actual se haya actualizado correctamente
        assertThat(objetivoActualizado.getMontoActual(), equalTo(7000.0)); // 5000.0 + 2000.0
    }
}