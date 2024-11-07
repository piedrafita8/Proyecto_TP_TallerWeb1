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
import java.time.LocalDate;
import java.util.List;

//import static com.tallerwebi.dominio.enums.TipoMovimiento.INGRESO;
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
        ingreso.setFecha(LocalDate.of(2022, 12, 20));


        // Guardar usando el repositorio (opcionalmente podrías usar sessionFactory)
        this.RepositorioIngreso.guardar(ingreso);

        // Hacer la consulta HQL para encontrar el egreso guardado
        String hql = "SELECT i FROM Ingreso i WHERE i.monto = :monto AND i.descripcion = :descripcion AND i.fecha = :fecha";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("monto", 400000.0);
        query.setParameter("descripcion", "Ingreso con origen de mi sueldo");
        query.setParameter("fecha", LocalDate.of(2022, 12, 20));

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
        ingreso1.setFecha(LocalDate.of(2022, 12, 20));
        ingreso1.setDescripcion("Ingreso de un prestamo bancario");
        
        Ingreso ingreso2 = new Ingreso();
        ingreso2.setMonto(80000.0);
        ingreso2.setFecha(LocalDate.of(2022, 12, 20));
        ingreso2.setDescripcion("Ingreso de dinero prestado de un familiar");
        
        Ingreso ingreso3 = new Ingreso();
        ingreso3.setMonto(199000.0);
        ingreso3.setFecha(LocalDate.of(2022, 12, 20));
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
    public void dadoQueExisteUnRepositorioIngresoCuandoGuardo3IngresosEntoncesEncuentroEsos3IngresosEnLaBaseDeDatos(){
        Ingreso ingreso1 = new Ingreso();
        ingreso1.setMonto(245000.0);
        ingreso1.setFecha(LocalDate.of(2022, 12, 20));
        ingreso1.setDescripcion("Ingreso de un prestamo bancario");
        
        Ingreso ingreso2 = new Ingreso();
        ingreso2.setMonto(80000.0);
        ingreso2.setFecha(LocalDate.of(2022, 12, 20));
        ingreso2.setDescripcion("Ingreso de dinero prestado de un familiar");
        
        Ingreso ingreso3 = new Ingreso();
        ingreso3.setMonto(199000.0);
        ingreso3.setFecha(LocalDate.of(2022, 12, 20));
        ingreso3.setDescripcion("Ingreso proveniente de beca");
        
        this.sessionFactory.getCurrentSession().save(ingreso1);
        this.sessionFactory.getCurrentSession().save(ingreso2);
        this.sessionFactory.getCurrentSession().save(ingreso3);

        List<Ingreso> ingresosObtenidos = this.RepositorioIngreso.obtener();

        Integer cantidadEsperada = 3;
        assertThat(ingresosObtenidos.size(), equalTo(cantidadEsperada));
        assertThat(ingresosObtenidos.get(0),equalTo(ingreso1));
        assertThat(ingresosObtenidos.get(1),equalTo(ingreso2));
        assertThat(ingresosObtenidos.get(2),equalTo(ingreso3));
    }


    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoActualizoUnIngresoEntoncesLoEncuentroEnLaBaseDeDatos(){
        LocalDate fechaIngreso = LocalDate.of(2022, 12, 20);
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(30000.0);
        ingreso.setFecha(fechaIngreso);
        ingreso.setDescripcion("Ingreso de inversiones");
        this.sessionFactory.getCurrentSession().save(ingreso);

        Double nuevoMonto = 37500.0;
        ingreso.setMonto(nuevoMonto);

        this.RepositorioIngreso.actualizar(ingreso);

        Query query = this.sessionFactory.getCurrentSession().createQuery("FROM Ingreso i WHERE i.monto = :monto AND i.fecha = :fecha AND i.descripcion = :descripcion");
        query.setParameter("monto", 37500.0);
        query.setParameter("fecha", fechaIngreso);
        query.setParameter("descripcion", "Ingreso de inversiones");

        Ingreso ingresoObtenido = (Ingreso) query.getSingleResult();

        assertThat(ingresoObtenido.getMonto(), equalTo(nuevoMonto));
        assertThat(ingresoObtenido.getFecha(), equalTo(fechaIngreso));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoCreoUnIngresoLuegoPuedoBorrarlo(){
        // Crear un objeto Egreso con el monto deseado
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(15000.0);
        ingreso.setDescripcion("Donaciones");
        ingreso.setFecha(LocalDate.of(2023, 11, 10));
        ingreso.setId(0);

        // Guardarlo usando el repositorio 
        this.RepositorioIngreso.guardar(ingreso);

        //Borrarlo
        this.RepositorioIngreso.eliminar(ingreso);

        List<Ingreso> egresosObtenidos = this.RepositorioIngreso.obtener();
        Integer cantidadEsperada = 0;
        

        //Verificar que el objeto se elimino correctamente del repositorio
        assertThat(egresosObtenidos.size(), equalTo(cantidadEsperada));
    }


    @Test
    @Transactional
    @Rollback
    public void dadoQueExisteUnRepositorioIngresoCuandoCreoUnIngresoLuegoPuedoBuscarloYObtenerloCorrectamente(){
        // Crear un objeto Egreso con el monto deseado
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(15000.0);
        ingreso.setDescripcion("Ingreso de inversiones cobradas");
        ingreso.setFecha(LocalDate.of(2023, 11, 10));

        // Guardarlo usando el repositorio 
        this.RepositorioIngreso.guardar(ingreso);
        sessionFactory.getCurrentSession().flush();
        Integer idGuardado=ingreso.getId();

        //Buscar egreso y verificar
        Ingreso egresObtenido=this.RepositorioIngreso.buscar(15000.0, idGuardado);
        assertThat(egresObtenido,equalTo(ingreso));
    
    }

    @Test
    @Transactional
    @Rollback
    public void dadoUnEgresoExistenteCuandoLoModificoEntoncesLosCambiosSeGuardanCorrectamente() {
        // Crear y guardar un objeto Egreso inicial
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(10000.0);
        ingreso.setDescripcion("Ingreso original");
        ingreso.setFecha(LocalDate.of(2023, 11, 1));
        this.RepositorioIngreso.guardar(ingreso);
    
        // Sincronizar para asegurar que el objeto esté persistido
        sessionFactory.getCurrentSession().flush();

        // Modificar el objeto
        ingreso.setMonto(12000.0);
        ingreso.setDescripcion("Ingreso modificado");

        // Llamar al método modificar
        this.RepositorioIngreso.modificar(ingreso);

        // Sincronizar para asegurar que la modificación se guarde
        sessionFactory.getCurrentSession().flush();

        // Recuperar el objeto modificado y verificar los cambios
        Ingreso ingresoModificado = this.RepositorioIngreso.buscar(12000.0, ingreso.getId());

        assertThat(ingresoModificado.getMonto(),equalTo(12000.0));
        assertThat(ingresoModificado.getDescripcion(),equalTo("Ingreso modificado"));
    }
}

