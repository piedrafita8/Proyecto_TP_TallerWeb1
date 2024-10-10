package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.enums.TipoIngreso;
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
import java.time.LocalDate;
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
        ingreso.setFecha(LocalDate.of(2021, 11, 14));
        ingreso.setTipoMovimiento(INGRESO);
        ingreso.setTipoIngreso(TipoIngreso.SALARIO);

        // Guardar usando el repositorio (opcionalmente podrías usar sessionFactory)
        this.RepositorioIngreso.guardar(ingreso);

        // Hacer la consulta HQL para encontrar el egreso guardado
        String hql = "SELECT i FROM Ingreso i WHERE i.monto = :monto AND i.descripcion = :descripcion AND i.fecha = :fecha AND i.tipoMovimiento = :tipoMovimiento AND i.tipoIngreso = :tipoIngreso ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("monto", 400000.0);
        query.setParameter("descripcion", "Ingreso con origen de mi sueldo");
        query.setParameter("fecha", LocalDate.of(2021, 11, 14));
        query.setParameter("tipoMovimiento", INGRESO);
        query.setParameter("tipoIngreso", TipoIngreso.SALARIO);

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
        ingreso1.setFecha(LocalDate.of(2023, 10, 16));
        ingreso1.setTipoMovimiento(INGRESO);
        ingreso1.setDescripcion("Ingreso de un prestamo bancario");
        ingreso1.setTipoIngreso(TipoIngreso.PRESTAMO);
        Ingreso ingreso2 = new Ingreso();
        ingreso2.setMonto(80000.0);
        ingreso2.setFecha(LocalDate.of(2024, 10, 10));
        ingreso2.setTipoMovimiento(INGRESO);
        ingreso2.setDescripcion("Ingreso de dinero prestado de un familiar");
        ingreso2.setTipoIngreso(TipoIngreso.PRESTAMO);
        Ingreso ingreso3 = new Ingreso();
        ingreso3.setMonto(20000.0);
        ingreso2.setFecha(LocalDate.of(2024, 5, 10));
        ingreso3.setTipoMovimiento(INGRESO);
        ingreso3.setDescripcion("Ingreso proveniente de beca");
        ingreso3.setTipoIngreso(TipoIngreso.BECA);
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
        ingreso.setFecha(LocalDate.of(2024, 10, 10));
        ingreso.setTipoMovimiento(INGRESO);
        ingreso.setDescripcion("Ingreso de inversiones");
        ingreso.setTipoIngreso(TipoIngreso.AHORROS);
        this.sessionFactory.getCurrentSession().save(ingreso);
        Double nuevoMonto = 37500.0;
        ingreso.setMonto(nuevoMonto);

        this.RepositorioIngreso.actualizar(ingreso);
        this.sessionFactory.getCurrentSession().save(ingreso);

       Query query = this.sessionFactory.getCurrentSession().createQuery("FROM Ingreso i WHERE i.monto = :monto AND i.fecha = :fecha AND i.descripcion = :descripcion AND i.tipoMovimiento = :tipoMovimiento AND i.tipoIngreso = :tipoIngreso ");
       query.setParameter("monto", 37500.0);
       query.setParameter("fecha", LocalDate.of(2024, 10, 10));
       query.setParameter("descripcion", "Ingreso de inversiones");
       query.setParameter("tipoMovimiento", INGRESO);
       query.setParameter("tipoIngreso", TipoIngreso.AHORROS);

       Ingreso ingresoObtenido = (Ingreso) query.getSingleResult();

        assertThat(ingresoObtenido.getMonto(), equalTo(nuevoMonto));
    }
}
