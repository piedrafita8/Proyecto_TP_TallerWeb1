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

import java.time.LocalDate;
import java.util.List;

import static com.tallerwebi.dominio.enums.TipoMovimiento.EGRESO;
import static java.time.LocalDate.parse;
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
        egreso.setFecha(LocalDate.of(2022, 12, 20));


        // Guardar usando el repositorio (opcionalmente podrías usar sessionFactory)
        this.RepositorioEgreso.guardar(egreso);

        // Hacer la consulta HQL para encontrar el egreso guardado
        String hql = "SELECT e FROM Egreso e WHERE e.monto = :monto AND e.descripcion = :descripcion AND e.fecha = :fecha";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("monto", 12000.0);
        query.setParameter("descripcion", "Gasto para pagar en la verduleria");
        query.setParameter("fecha", (LocalDate.of(2022, 12, 20)));

        // Obtener el resultado de la consulta
        Egreso egresoObtenido = (Egreso) query.getSingleResult();

        // Verificar que el egreso guardado es el mismo que el obtenido
        assertThat(egresoObtenido, equalTo(egreso));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioEgresoCuandoGuardo3EgresosEntoncesEncuentro3EgresosEnLaBaseDeDatos(){
        Egreso egreso1 = new Egreso();
        egreso1.setMonto(27900.0);
        egreso1.setFecha(LocalDate.of(2022, 12, 20));
        egreso1.setDescripcion("Egreso de un prestamo bancario");
        Egreso egreso2 = new Egreso();
        egreso2.setMonto(88900.0);
        egreso2.setFecha(LocalDate.of(2022, 12, 20));
        egreso2.setDescripcion("Egreso de dinero prestado de un familiar");
        Egreso egreso3 = new Egreso();
        egreso3.setMonto(95000.0);
        egreso3.setFecha(LocalDate.of(2022, 12, 20));
        egreso3.setDescripcion("Egreso proveniente de beca");
        this.sessionFactory.getCurrentSession().save(egreso1);
        this.sessionFactory.getCurrentSession().save(egreso2);
        this.sessionFactory.getCurrentSession().save(egreso3);

        List<Egreso> egresosObtenidos = this.RepositorioEgreso.obtener();

        Integer cantidadEsperada = 3;
        assertThat(egresosObtenidos.size(), equalTo(cantidadEsperada));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioEgresoCuandoActualizoUnIngresoEntoncesLoEncuentroEnLaBaseDeDatos(){
        Egreso egreso = new Egreso();
        egreso.setMonto(30000.0);
        egreso.setFecha(LocalDate.of(2022, 12, 20));
        egreso.setDescripcion("Gasto para pagar kiosko");
        this.sessionFactory.getCurrentSession().save(egreso);
        String nuevaDescripcion = "Gasto para pagar almacen";
        egreso.setDescripcion(nuevaDescripcion);

        this.RepositorioEgreso.actualizar(egreso);
        this.sessionFactory.getCurrentSession().save(egreso);

        Query query = this.sessionFactory.getCurrentSession().createQuery("FROM Egreso e WHERE e.monto = :monto AND e.fecha = :fecha AND e.descripcion = :descripcion");
        query.setParameter("monto", 30000.0);
        query.setParameter("fecha",LocalDate.of(2022, 12, 20));
        query.setParameter("descripcion", "Gasto para pagar almacen");

        Egreso egresosObtenido = (Egreso) query.getSingleResult();

        assertThat(egresosObtenido.getDescripcion(), equalTo(nuevaDescripcion));
    }
}
