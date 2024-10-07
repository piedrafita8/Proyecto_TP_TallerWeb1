package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.interfaces.RepositorioIngreso;
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

import static com.tallerwebi.dominio.enums.TipoMovimiento.EGRESO;
import static com.tallerwebi.dominio.enums.TipoMovimiento.INGRESO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioIngresoImplTest{

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioIngreso RepositorioIngreso;

    @BeforeEach
    public void init(){
        this.RepositorioIngreso = new RepositorioIngresoImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoAgregoUnIngresoConMonto400000EntoncesLoEncuentroEnLaBaseDeDatos(){
        // Crear un objeto Egreso con el monto deseado
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(400000.0);
        ingreso.setDescripcion("Ingreso con origen de mi sueldo");
        ingreso.setFecha(28092024);
        ingreso.setTipo_movimiento(INGRESO);

        // Guardar usando el repositorio (opcionalmente podrías usar sessionFactory)
        this.RepositorioIngreso.guardar(ingreso);

        // Hacer la consulta HQL para encontrar el egreso guardado
        String hql = "SELECT i FROM Ingreso i WHERE i.monto = :monto AND i.descripcion = :descripcion AND i.fecha = :fecha";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("monto", 400000.0);
        query.setParameter("descripcion", "Ingreso con origen de mi sueldo");
        query.setParameter("fecha", 28092024);

        // Obtener el resultado de la consulta
        Ingreso ingresoObtenido = (Ingreso) query.getSingleResult();

        // Verificar que el egreso guardado es el mismo que el obtenido
        assertThat(ingresoObtenido, equalTo(ingreso));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoGuardo3IngresosEntoncesEncuentro3IngresosEnLaBaseDeDatos(){
        Ingreso ingreso1 = new Ingreso();
        ingreso1.setMonto(245000.0);
        ingreso1.setFecha(16092024);
        ingreso1.setTipo_movimiento(INGRESO);
        ingreso1.setDescripcion("Ingreso de un prestamo bancario");
        Ingreso ingreso2 = new Ingreso();
        ingreso2.setMonto(80000.0);
        ingreso2.setFecha(11092024);
        ingreso2.setTipo_movimiento(INGRESO);
        ingreso2.setDescripcion("Ingreso de dinero prestado de un familiar");
        Ingreso ingreso3 = new Ingreso();
        ingreso3.setMonto(199000.0);
        ingreso3.setFecha(1092024);
        ingreso3.setTipo_movimiento(INGRESO);
        ingreso3.setDescripcion("Ingreso proveniente de beca");
        this.sessionFactory.getCurrentSession().save(ingreso1);
        this.sessionFactory.getCurrentSession().save(ingreso2);
        this.sessionFactory.getCurrentSession().save(ingreso3);

        List<Ingreso> ingresosObtenidos = this.RepositorioIngreso.obtener();

        Integer cantidadEsperada = 3;
        assertThat(ingresosObtenidos.size(), equalTo(cantidadEsperada));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoActualizoUnIngresoEntoncesLoEncuentroEnLaBaseDeDatos(){
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(30000.0);
        ingreso.setFecha(28092024);
        ingreso.setTipo_movimiento(INGRESO);
        ingreso.setDescripcion("Ingreso de inversiones");
        this.sessionFactory.getCurrentSession().save(ingreso);
        Double nuevoMonto = 37500.0;
        ingreso.setMonto(nuevoMonto);

        this.RepositorioIngreso.actualizar(ingreso);
        this.sessionFactory.getCurrentSession().save(ingreso);

       Query query = this.sessionFactory.getCurrentSession().createQuery("FROM Ingreso i WHERE i.monto = :monto AND i.fecha = :fecha AND i.descripcion = :descripcion");
       query.setParameter("monto", 37500.0);
       query.setParameter("fecha", 28092024);
       query.setParameter("descripcion", "Ingreso de inversiones");

       Ingreso ingresoObtenido = (Ingreso) query.getSingleResult();

        assertThat(ingresoObtenido.getMonto(), equalTo(nuevoMonto));
    }
}
