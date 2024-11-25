package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.enums.TipoMovimiento;
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
    public void dadoQueExistenTransaccionesCuandoLasGuardoEntoncesPuedoRecuperarlas() {
        
        Transaccion transaccion1 = new Transaccion(1,100.0, LocalDate.now(), TipoMovimiento.INGRESO, "Pago de factura", 1L);
        Transaccion transaccion2 = new Transaccion(2,150.0,LocalDate.now(), TipoMovimiento.INGRESO,   "Compra de productos", 2L);
        Transaccion transaccion3 = new Transaccion(3,200.0, LocalDate.now(), TipoMovimiento.EGRESO,"Pago de alquiler",  3L);

        repositorioTransaccion.guardar(transaccion1);
        repositorioTransaccion.guardar(transaccion2);
        repositorioTransaccion.guardar(transaccion3);

        List<Transaccion> transacciones = repositorioTransaccion.obtener();


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
    void dadoTransaccionDeTipoIngresoCuandoSeAgregaDebeSerRecuperadaCorrectamente() {
        // Crear una transacción de tipo INGRESO
        Transaccion transaccionIngreso = new Transaccion(1, 500.0, LocalDate.now(), TipoMovimiento.INGRESO, "Ingreso único", 1L);

        // Guardar la transacción
        repositorioTransaccion.guardar(transaccionIngreso);

        // Buscar transacciones por usuario
        List<Transaccion> transacciones = repositorioTransaccion.buscarTransaccionPorUsuario(1L);

        // Validar
        assertEquals(1, transacciones.size());
        assertThat(transacciones.get(0).getTipoMovimiento(), equalTo(TipoMovimiento.INGRESO));
    }

    @Test
    @Transactional
    @Rollback
    void dadoMontoEIdEspecificosCuandoBuscoTransaccionDebeDevolverTransaccionCorrecta() {
        // Crear una transacción
        Transaccion transaccionEsperada = new Transaccion(1, 100.0, LocalDate.now(), TipoMovimiento.INGRESO, "Ingreso esperado", 1L);

        // Guardar la transacción
        repositorioTransaccion.guardar(transaccionEsperada);

        // Buscar la transacción
        Transaccion result = repositorioTransaccion.buscar(100.0, 1);

        // Validar
        assertEquals(transaccionEsperada.getMonto(), result.getMonto());
        assertEquals(transaccionEsperada.getComentario(), result.getComentario());
    }*/
}