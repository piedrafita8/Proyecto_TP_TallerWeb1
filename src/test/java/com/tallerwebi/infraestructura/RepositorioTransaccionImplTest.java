package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaces.RepositorioTransaccion;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.models.Transaccion;
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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioTransaccionImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioTransaccionImpl repositorioTransaccion;

    @BeforeEach
    public void init() {
        this.repositorioTransaccion = new RepositorioTransaccionImpl(sessionFactory);
    }
/* 
    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoAgregoUnIngresoConMonto400000EntoncesLoEncuentroEnLaBaseDeDatos(){
        // Crear un objeto Egreso con el monto deseado
        Transaccion ingreso = new Ingreso();
        ingreso.setMonto(400000.0);
        ingreso.setDescripcion("Ingreso con origen de mi sueldo");
        ingreso.setFecha(LocalDate.of(2022, 12, 20));


        // Verificar que los datos coinciden con lo que se guardó
        assertThat(transacciones.size(), equalTo(3));
        assertThat(transacciones.get(0).getMonto(), equalTo(100.0));
        assertThat(transacciones.get(1).getMonto(), equalTo(150.0));
        assertThat(transacciones.get(2).getMonto(), equalTo(200.0));
    }



*/





    
/*     @Test
    @Transactional
    @Rollback
    void dadoRepositorioVacioCuandoGuardoTresTransaccionesLasEncuentroEnBaseDeDatos() {
        // Crear transacciones
        Transaccion transaccion1 = new Transaccion(1, 100.0, LocalDate.now(), TipoMovimiento.INGRESO, "Ingreso 1", 1L);
        Transaccion transaccion2 = new Transaccion(2, 200.0, LocalDate.now(), TipoMovimiento.EGRESO, "Egreso 1", 3L);
        Transaccion transaccion3 = new Transaccion(3, 300.0, LocalDate.now(), TipoMovimiento.INGRESO, "Ingreso 2", 2L);

        // Guardar las transacciones
        repositorioTransaccion.guardar(transaccion1);
        repositorioTransaccion.guardar(transaccion2);
        repositorioTransaccion.guardar(transaccion3);

        // Recuperar las transacciones
        List<Transaccion> transacciones = repositorioTransaccion.obtener();

        // Validar
        assertEquals(3, transacciones.size());
        assertThat(transacciones.get(0).getComentario(), equalTo("Ingreso 1"));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoGuardo3IngresosEntoncesEncuentro3IngresosEnLaBaseDeDatos(){
        Transaccion ingreso1 = new Ingreso();
        ingreso1.setMonto(245000.0);
        ingreso1.setFecha(LocalDate.of(2022, 12, 20));
        ingreso1.setDescripcion("Ingreso de un prestamo bancario");

        Transaccion ingreso2 = new Ingreso();
        ingreso2.setMonto(80000.0);
        ingreso2.setFecha(LocalDate.of(2022, 12, 20));
        ingreso2.setDescripcion("Ingreso de dinero prestado de un familiar");

        Transaccion ingreso3 = new Ingreso();
        ingreso3.setMonto(199000.0);
        ingreso3.setFecha(LocalDate.of(2022, 12, 20));
        ingreso3.setDescripcion("Ingreso proveniente de beca");

        // Validar
        assertEquals(1, transacciones.size());
        assertThat(transacciones.get(0).getTipoMovimiento(), equalTo(TipoMovimiento.INGRESO));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoGuardo3IngresosEntoncesEncuentroEsos3IngresosEnLaBaseDeDatos(){
        Transaccion ingreso1 = new Ingreso();
        ingreso1.setMonto(245000.0);
        ingreso1.setFecha(LocalDate.of(2022, 12, 20));
        ingreso1.setDescripcion("Ingreso de un prestamo bancario");

        Transaccion ingreso2 = new Ingreso();
        ingreso2.setMonto(80000.0);
        ingreso2.setFecha(LocalDate.of(2022, 12, 20));
        ingreso2.setDescripcion("Ingreso de dinero prestado de un familiar");

        Transaccion ingreso3 = new Ingreso();
        ingreso3.setMonto(199000.0);
        ingreso3.setFecha(LocalDate.of(2022, 12, 20));
        ingreso3.setDescripcion("Ingreso proveniente de beca");

        this.sessionFactory.getCurrentSession().save(ingreso1);
        this.sessionFactory.getCurrentSession().save(ingreso2);
        this.sessionFactory.getCurrentSession().save(ingreso3);

        List<Transaccion> transaccionesObtenidos = this.repositorioTransaccion.obtener();

        Integer cantidadEsperada = 3;
        assertThat(transaccionesObtenidos.size(), equalTo(cantidadEsperada));
        assertThat(transaccionesObtenidos.get(0),equalTo(ingreso1));
        assertThat(transaccionesObtenidos.get(1),equalTo(ingreso2));
        assertThat(transaccionesObtenidos.get(2),equalTo(ingreso3));
    }


    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoActualizoUnIngresoEntoncesLoEncuentroEnLaBaseDeDatos(){
        LocalDate fechaIngreso = LocalDate.of(2022, 12, 20);
        Transaccion ingreso = new Ingreso();
        ingreso.setMonto(30000.0);
        ingreso.setFecha(fechaIngreso);
        ingreso.setDescripcion("Ingreso de inversiones");
        this.sessionFactory.getCurrentSession().save(ingreso);

        Double nuevoMonto = 37500.0;
        ingreso.setMonto(nuevoMonto);

        this.repositorioTransaccion.actualizar(ingreso);

        Query query = this.sessionFactory.getCurrentSession().createQuery("FROM Ingreso i WHERE i.monto = :monto AND i.fecha = :fecha AND i.descripcion = :descripcion");
        query.setParameter("monto", 37500.0);
        query.setParameter("fecha", fechaIngreso);
        query.setParameter("descripcion", "Ingreso de inversiones");

        Transaccion transaccionesObtenidos = (Transaccion) query.getSingleResult();

        assertThat(transaccionesObtenidos.getMonto(), equalTo(nuevoMonto));
        assertThat(transaccionesObtenidos.getFecha(), equalTo(fechaIngreso));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoCreoUnIngresoLuegoPuedoBorrarlo(){
        // Crear un objeto Egreso con el monto deseado
        Transaccion ingreso = new Ingreso();
        ingreso.setMonto(15000.0);
        ingreso.setDescripcion("Donaciones");
        ingreso.setFecha(LocalDate.of(2023, 11, 10));
        ingreso.setId(0);

        // Guardarlo usando el repositorio
        this.repositorioTransaccion.guardar(ingreso);

        //Borrarlo
        this.repositorioTransaccion.eliminar(ingreso);

        List<Transaccion> egresosObtenidos = this.repositorioTransaccion.obtener();
        Integer cantidadEsperada = 0;

        //Verificar que el objeto se elimino correctamente del repositorio
        assertThat(egresosObtenidos.size(), equalTo(cantidadEsperada));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoCreoUnIngresoLuegoPuedoBuscarloYObtenerloCorrectamente(){
        // Crear un objeto Egreso con el monto deseado
        Transaccion ingreso = new Ingreso();
        ingreso.setMonto(15000.0);
        ingreso.setDescripcion("Ingreso de inversiones cobradas");
        ingreso.setFecha(LocalDate.of(2023, 11, 10));

        // Guardarlo usando el repositorio
        this.repositorioTransaccion.guardar(ingreso);
        sessionFactory.getCurrentSession().flush();
        Integer idGuardado = ingreso.getId();

        //Buscar egreso y verificar
        Transaccion ingresoObtenido = this.repositorioTransaccion.buscar(15000.0, idGuardado);
        assertThat(ingresoObtenido,equalTo(ingreso));

    }

    @Test
   @Transactional
    @Rollback
    public void dadoUnIngresoExistenteCuandoLoModificoEntoncesLosCambiosSeGuardanCorrectamente() {
        // Crear y guardar un objeto Egreso inicial
        Transaccion ingreso = new Ingreso();
        ingreso.setMonto(10000.0);
        ingreso.setDescripcion("Ingreso original");
        ingreso.setFecha(LocalDate.of(2023, 11, 1));
        this.repositorioTransaccion.guardar(ingreso);

        // Sincronizar para asegurar que el objeto esté persistido
        sessionFactory.getCurrentSession().flush();

        // Modificar el objeto
        ingreso.setMonto(12000.0);
        ingreso.setDescripcion("Ingreso modificado");

        // Llamar al metodo modificar
        this.repositorioTransaccion.modificar(ingreso);

        // Sincronizar para asegurar que la modificación se guarde
        sessionFactory.getCurrentSession().flush();

        // Recuperar el objeto modificado y verificar los cambios
        Transaccion ingresoModificado = this.repositorioTransaccion.buscar(12000.0, ingreso.getId());

        assertThat(ingresoModificado.getMonto(),equalTo(12000.0));
        assertThat(ingresoModificado.getDescripcion(),equalTo("Ingreso modificado"));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioEgresoCuandoIngresoUnGastoConMonto12000EntoncesLoEncuentroEnLaBaseDeDatos(){
        // Crear un objeto Egreso con el monto deseado
        Transaccion egreso = new Egreso();
        egreso.setMonto(12000.0);
        egreso.setDescripcion("Gasto para pagar en la verduleria");
        egreso.setFecha(LocalDate.of(2022, 12, 20));


        // Guardar usando el repositorio (opcionalmente podrías usar sessionFactory)
       this.repositorioTransaccion.guardar(egreso);

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
        Transaccion egreso1 = new Egreso();
        egreso1.setMonto(27900.0);
        egreso1.setFecha(LocalDate.of(2022, 12, 20));
        egreso1.setDescripcion("Egreso de un prestamo bancario");
        Transaccion egreso2 = new Egreso();
        egreso2.setMonto(88900.0);
        egreso2.setFecha(LocalDate.of(2022, 12, 20));
        egreso2.setDescripcion("Egreso de dinero prestado de un familiar");
        Transaccion egreso3 = new Egreso();
        egreso3.setMonto(95000.0);
        egreso3.setFecha(LocalDate.of(2022, 12, 20));
        egreso3.setDescripcion("Egreso proveniente de beca");
        this.sessionFactory.getCurrentSession().save(egreso1);
        this.sessionFactory.getCurrentSession().save(egreso2);
        this.sessionFactory.getCurrentSession().save(egreso3);

        List<Transaccion> egresosObtenidos = this.repositorioTransaccion.obtener();

        Integer cantidadEsperada = 3;
        assertThat(egresosObtenidos.size(), equalTo(cantidadEsperada));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioEgresoCuandoActualizoUnIngresoEntoncesLoEncuentroEnLaBaseDeDatos(){
        Transaccion egreso = new Egreso();
        egreso.setMonto(30000.0);
        egreso.setFecha(LocalDate.of(2022, 12, 20));
        egreso.setDescripcion("Gasto para pagar kiosko");
        this.sessionFactory.getCurrentSession().save(egreso);
        String nuevaDescripcion = "Gasto para pagar almacen";
        egreso.setDescripcion(nuevaDescripcion);

        this.repositorioTransaccion.actualizar(egreso);
        this.sessionFactory.getCurrentSession().save(egreso);

        Query query = this.sessionFactory.getCurrentSession().createQuery("FROM Egreso e WHERE e.monto = :monto AND e.fecha = :fecha AND e.descripcion = :descripcion");
        query.setParameter("monto", 30000.0);
        query.setParameter("fecha",LocalDate.of(2022, 12, 20));
        query.setParameter("descripcion", "Gasto para pagar almacen");

        Egreso egresosObtenido = (Egreso) query.getSingleResult();
        assertThat(egresosObtenido.getDescripcion(), equalTo(nuevaDescripcion));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioEgresoCuandoCreoUnEgresoLuegoPuedoBorrarlo(){
        // Crear un objeto Egreso con el monto deseado
        Transaccion egreso = new Egreso();
        egreso.setMonto(15000.0);
        egreso.setDescripcion("Gasto para pagar chocolates");
        egreso.setFecha(LocalDate.of(2023, 11, 10));
        egreso.setId(0);

        // Guardarlo usando el repositorio
        this.repositorioTransaccion.guardar(egreso);

        //Borrarlo
        this.repositorioTransaccion.eliminar(egreso);

        List<Transaccion> egresosObtenidos = this.repositorioTransaccion.obtener();
        Integer cantidadEsperada = 0;


        //Verificar que el objeto se elimino correctamente del repositorio
        assertThat(egresosObtenidos.size(), equalTo(cantidadEsperada));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioEgresoCuandoCreoUnEgresoLuegoPuedoBuscarloYObtenerloCorrectamente(){
        // Crear un objeto Egreso con el monto deseado
        Transaccion egreso = new Egreso();
        egreso.setMonto(15000.0);
        egreso.setDescripcion("Gasto para pagar chocolates");
        egreso.setFecha(LocalDate.of(2023, 11, 10));

        // Guardarlo usando el repositorio
        this.repositorioTransaccion.guardar(egreso);
        sessionFactory.getCurrentSession().flush();
        Integer idGuardado=egreso.getId();

        //Buscar egreso y verificar
        Transaccion egresObtenido = this.repositorioTransaccion.buscar(15000.0, idGuardado);
        assertThat(egresObtenido,equalTo(egreso));

    }

    @Test
    @Transactional
    @Rollback

    public void dadoUnEgresoExistenteCuandoLoModificoEntoncesLosCambiosSeGuardanCorrectamente() {
        // Crear y guardar un objeto Egreso inicial
        Transaccion egreso = new Egreso();
        egreso.setMonto(10000.0);
        egreso.setDescripcion("Gasto original");
        egreso.setFecha(LocalDate.of(2023, 11, 1));
        this.repositorioTransaccion.guardar(egreso);

        // Sincronizar para asegurar que el objeto esté persistido
        sessionFactory.getCurrentSession().flush();

        // Modificar el objeto
        egreso.setMonto(12000.0);
        egreso.setDescripcion("Gasto modificado");

        // Llamar al metodo modificar
        this.repositorioTransaccion.modificar(egreso);

        // Sincronizar para asegurar que la modificación se guarde
        sessionFactory.getCurrentSession().flush();

        // Recuperar el objeto modificado y verificar los cambios
        Transaccion egresoModificado = this.repositorioTransaccion.buscar(12000.0, egreso.getId());

        assertThat(egresoModificado.getMonto(),equalTo(12000.0));
        assertThat(egresoModificado.getDescripcion(),equalTo("Gasto modificado"));
    }
}

        // Validar
        assertEquals(transaccionEsperada.getMonto(), result.getMonto());
        assertEquals(transaccionEsperada.getComentario(), result.getComentario());
    }*/
}