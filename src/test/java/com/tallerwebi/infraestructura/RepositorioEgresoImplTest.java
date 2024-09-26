package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Egreso;
import com.tallerwebi.dominio.RepositorioEgreso;
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
    public void dadoQueExisteUnRepositorioEgresoCuandoIngresoUnGastoConMonto12000EntoncesLoEncuentroEnLaBaseDeDatos(){
        // Crear un objeto Egreso con el monto deseado
        Egreso egreso = new Egreso();
        egreso.setMonto(12000.0);

        // Guardar usando el repositorio (opcionalmente podrías usar sessionFactory)
        this.repositorioEgreso.guardar(egreso);

        // Hacer la consulta HQL para encontrar el egreso guardado
        String hql = "SELECT e FROM Egreso e WHERE e.monto = :monto";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("monto", 12000.0);

        // Obtener el resultado de la consulta
        Egreso egresoObtenido = (Egreso) query.getSingleResult();

        // Verificar que el egreso guardado es el mismo que el obtenido
        assertThat(egresoObtenido, equalTo(egreso));
    }
}
