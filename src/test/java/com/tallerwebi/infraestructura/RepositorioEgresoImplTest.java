package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.interfaces.RepositorioEgreso;
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

import static com.tallerwebi.dominio.enums.TipoMovimiento.EGRESO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioEgresoImplTest{

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioEgreso RepositorioEgreso;

    @BeforeEach
    public void init(){
        this.RepositorioEgreso = new RepositorioEgresoImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioEgresoCuandoIngresoUnGastoConMonto12000EntoncesLoEncuentroEnLaBaseDeDatos(){
        // Crear un objeto Egreso con el monto deseado
        Egreso egreso = new Egreso();
        egreso.setMonto(12000.0);
        egreso.setDescripcion("Gasto para pagar en la verduleria");
        egreso.setFecha(21092024);
        egreso.setTipo_movimiento(EGRESO);

        // Guardar usando el repositorio (opcionalmente podrías usar sessionFactory)
        this.RepositorioEgreso.guardar(egreso);

        // Hacer la consulta HQL para encontrar el egreso guardado
        String hql = "SELECT e FROM Egreso e WHERE e.monto = :monto AND e.descripcion = :descripcion AND e.fecha = :fecha";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("monto", 12000.0);
        query.setParameter("descripcion", "Gasto para pagar en la verduleria");
        query.setParameter("fecha", 21092024);

        // Obtener el resultado de la consulta
        Egreso egresoObtenido = (Egreso) query.getSingleResult();

        // Verificar que el egreso guardado es el mismo que el obtenido
        assertThat(egresoObtenido, equalTo(egreso));
    }
}
