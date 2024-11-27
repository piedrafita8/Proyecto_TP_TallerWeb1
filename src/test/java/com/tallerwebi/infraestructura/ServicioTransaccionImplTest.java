package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.models.*;
import com.tallerwebi.dominio.interfaces.*;
import com.tallerwebi.dominio.excepcion.*;

import com.tallerwebi.dominio.servicios.ServicioTransaccionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioTransaccionImplTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @Mock
    private RepositorioTransaccion repositorioTransaccion;

    private ServicioTransaccionImpl servicioTransaccion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servicioTransaccion = new ServicioTransaccionImpl(
                repositorioUsuario,
                repositorioTransaccion
        );
    }

    @Test
    void consultarTransaccion_cuandoTransaccionExiste_deberiaRetornarTransaccion() throws RecursoNoEncontrado {
        // Preparación
        Double monto = 500.0;
        Integer id = 1;

        Transaccion transaccionEsperada = new Egreso();
        transaccionEsperada.setMonto(monto);
        transaccionEsperada.setId(id);

        when(repositorioTransaccion.buscar(monto, id)).thenReturn(transaccionEsperada);

        // Ejecución
        Transaccion transaccionRecuperada = servicioTransaccion.consultarTransaccion(monto, id);

        // Verificación
        assertNotNull(transaccionRecuperada);
        assertEquals(monto, transaccionRecuperada.getMonto());
        assertEquals(id, transaccionRecuperada.getId());
    }

    @Test
    void consultarTransaccion_cuandoTransaccionNoExiste_deberiaLanzarExcepcion() {
        // Preparación
        Double monto = 500.0;
        Integer id = 1;

        when(repositorioTransaccion.buscar(monto, id)).thenReturn(null);

        // Ejecución y Verificación
        assertThrows(RecursoNoEncontrado.class, () ->
                servicioTransaccion.consultarTransaccion(monto, id)
        );
    }

    @Test
    void consultarTransaccion_cuandoTransaccionTieneMontoInvalido_deberiaLanzarExcepcion() {
        // Preparación
        Double monto = -500.0;
        Integer id = 1;

        Transaccion transaccionInvalida = new Egreso();
        transaccionInvalida.setMonto(monto);
        transaccionInvalida.setId(id);

        when(repositorioTransaccion.buscar(monto, id)).thenReturn(transaccionInvalida);

        // Ejecución y Verificación
        assertThrows(RecursoNoEncontrado.class, () ->
                servicioTransaccion.consultarTransaccion(monto, id)
        );
    }

    @Test
    void crearTransaccion_cuandoEsIngreso_deberiaAgregarTransaccionYActualizarSaldo() throws SaldoInsuficiente {
        // Preparación
        Long userId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setSaldo(1000.0);

        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(500.0);

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(usuario);

        // Ejecución
        servicioTransaccion.crearTransaccion(ingreso, userId);

        // Verificación
        assertEquals(1500.0, usuario.getSaldo());
        verify(repositorioTransaccion).guardar(ingreso);
        verify(repositorioUsuario).modificar(usuario);
        assertTrue(usuario.getTransacciones().contains(ingreso));
    }

    @Test
    void crearTransaccion_cuandoEsEgreso_conSaldoSuficiente_deberiaAgregarTransaccionYActualizarSaldo() throws SaldoInsuficiente {
        // Preparación
        Long userId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setSaldo(1000.0);

        Egreso egreso = new Egreso();
        egreso.setMonto(500.0);

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(usuario);

        // Ejecución
        servicioTransaccion.crearTransaccion(egreso, userId);

        // Verificación
        assertEquals(500.0, usuario.getSaldo());
        verify(repositorioTransaccion).guardar(egreso);
        verify(repositorioUsuario).modificar(usuario);
        assertTrue(usuario.getTransacciones().contains(egreso));
    }

    @Test
    void crearTransaccion_cuandoEsEgreso_conSaldoInsuficiente_deberiaLanzarExcepcion() {
        // Preparación
        Long userId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setSaldo(200.0);

        Egreso egreso = new Egreso();
        egreso.setMonto(500.0);

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(usuario);

        // Ejecución y Verificación
        assertThrows(SaldoInsuficiente.class, () ->
                servicioTransaccion.crearTransaccion(egreso, userId)
        );
    }

    @Test
    void crearTransaccion_cuandoElUsuarioEsNulo_deberiaLanzarExcepcion() {
        // Preparación
        Long userId = 1L;
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(500.0);

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(null);

        // Ejecución y Verificación
        assertThrows(IllegalArgumentException.class, () ->
                servicioTransaccion.crearTransaccion(ingreso, userId)
        );
    }

    @Test
    void crearTransaccion_cuandoLaTransaccionEsNula_deberiaLanzarExcepcion() {
        // Preparación
        Long userId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(userId);

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(usuario);

        // Ejecución y Verificación
        assertThrows(IllegalArgumentException.class, () ->
                servicioTransaccion.crearTransaccion(null, userId)
        );
    }

    @Test
    void getAllTransacciones_deberiaRetornarTodasLasTransacciones() {
        // Preparación
        List<Transaccion> transaccionesEsperadas = Arrays.asList(
                new Ingreso(),
                new Egreso()
        );

        when(repositorioTransaccion.obtener()).thenReturn(transaccionesEsperadas);

        // Ejecución
        List<Transaccion> transaccionesRecuperadas = servicioTransaccion.getAllTransacciones();

        // Verificación
        assertFalse(transaccionesRecuperadas.isEmpty());
        assertEquals(transaccionesEsperadas, transaccionesRecuperadas);
    }

    @Test
    void getTransaccionPorUserId_deberiaRetornarTransaccionesDelUsuario() {
        // Preparación
        Long userId = 1L;
        List<Transaccion> transaccionesEsperadas = Collections.singletonList(new Ingreso());

        when(repositorioTransaccion.buscarTransaccionPorUsuario(userId))
                .thenReturn(transaccionesEsperadas);

        // Ejecución
        List<Transaccion> transaccionesRecuperadas = servicioTransaccion.getTransaccionPorUserId(userId);

        // Verificación
        assertFalse(transaccionesRecuperadas.isEmpty());
        assertEquals(transaccionesEsperadas, transaccionesRecuperadas);
    }

    @Test
    void obtenerTodasLasTransaccionesPorUserId_deberiaRetornarTransaccionesDelUsuario() {
        // Preparación
        Long userId = 1L;
        List<Transaccion> transaccionesEsperadas = Collections.singletonList(new Ingreso());

        when(repositorioTransaccion.obtenerTodasLasTransaccionesPorUserId(userId))
                .thenReturn(transaccionesEsperadas);

        // Ejecución
        List<Transaccion> transaccionesRecuperadas = servicioTransaccion.obtenerTodasLasTransaccionesPorUserId(userId);

        // Verificación
        assertFalse(transaccionesRecuperadas.isEmpty());
        assertEquals(transaccionesEsperadas, transaccionesRecuperadas);
    }

    @Test
    void registrarTransaccionSinActualizarSaldo_deberiaGuardarTransaccion() {
        // Preparación
        Transaccion transaccion = new Ingreso();

        // Ejecución
        servicioTransaccion.registrarTransaccionSinActualizarSaldo(transaccion);

        // Verificación
        verify(repositorioTransaccion).guardar(transaccion);
    }
}