package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.enums.CategoriaObjetivo;
import com.tallerwebi.dominio.models.*;
import com.tallerwebi.dominio.interfaces.*;
import com.tallerwebi.dominio.excepcion.*;

import com.tallerwebi.dominio.servicios.ServicioObjetivoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioObjetivoImplTest {

    @Mock
    private RepositorioObjetivo repositorioObjetivo;

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @Mock
    private ServicioTransaccion servicioTransaccion;

    private ServicioObjetivoImpl servicioObjetivo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servicioObjetivo = new ServicioObjetivoImpl(
                repositorioObjetivo,
                repositorioUsuario,
                servicioTransaccion
        );
    }

    @Test
    void crearObjetivo_cuandoElUsuarioExiste_deberiaCrearObjetivo() {
        // Preparación
        Long userId = 1L;
        String nombre = "Casa Nueva";
        Double montoObjetivo = 75000.0;
        Date fechaLimite = new Date();
        CategoriaObjetivo categoriaObjetivo = CategoriaObjetivo.VIAJE;

        Usuario usuario = new Usuario();
        usuario.setId(userId);

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(usuario);

        // Ejecución
        assertDoesNotThrow(() ->
                servicioObjetivo.crearObjetivo(nombre, montoObjetivo, fechaLimite, categoriaObjetivo, userId)
        );

        // Verificación
        verify(repositorioObjetivo).crearObjetivo(any(Objetivo.class));
    }

    @Test
    void crearObjetivo_cuandoElUsuarioNoExiste_deberiaLanzarExcepcion() {
        // Preparación
        Long userId = 1L;
        String nombre = "Casa Nueva";
        Double montoObjetivo = 75000.0;
        Date fechaLimite = new Date();
        CategoriaObjetivo categoriaObjetivo = CategoriaObjetivo.VIAJE;

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(null);

        // Ejecución y Verificación
        assertThrows(IllegalArgumentException.class, () ->
                servicioObjetivo.crearObjetivo(nombre, montoObjetivo, fechaLimite, categoriaObjetivo, userId)
        );
    }

    @Test
    void buscarObjetivosPorFiltros_cuandoElUsuarioExiste_deberiaRetornarObjetivos() {
        // Preparación
        String emailUsuario = "usuario@test.com";
        CategoriaObjetivo categoria = CategoriaObjetivo.VIAJE;

        Usuario usuario = new Usuario();
        when(repositorioUsuario.buscar(emailUsuario)).thenReturn(usuario);

        List<Objetivo> objetivosEsperados = Collections.singletonList(new Objetivo());
        when(repositorioObjetivo.buscarObjetivosPorFiltros(usuario, categoria))
                .thenReturn(objetivosEsperados);

        // Ejecución
        List<Objetivo> resultado = servicioObjetivo.buscarObjetivosPorFiltros(emailUsuario, categoria);

        // Verificación
        assertFalse(resultado.isEmpty());
        assertEquals(objetivosEsperados, resultado);
    }

    @Test
    void buscarObjetivosPorFiltros_cuandoElUsuarioNoExiste_deberiaRetornarListaVacia() {
        // Preparación
        String emailUsuario = "usuario@test.com";
        CategoriaObjetivo categoria = CategoriaObjetivo.VIAJE;

        when(repositorioUsuario.buscar(emailUsuario)).thenReturn(null);

        // Ejecución
        List<Objetivo> resultado = servicioObjetivo.buscarObjetivosPorFiltros(emailUsuario, categoria);

        // Verificación
        assertTrue(resultado.isEmpty());
    }

    @Test
    void actualizarObjetivo_cuandoElSaldoEsSuficiente_deberiaActualizarObjetivo() throws SaldoInsuficiente {
        // Preparación
        Integer objetivoId = 1;
        Long userId = 1L;
        Double montoAAgregar = 500.0;

        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setSaldo(1000.0);

        Objetivo objetivo = new Objetivo();
        objetivo.setId(objetivoId);
        objetivo.setNombre("Viaje");
        objetivo.setMontoActual(0.0);
        objetivo.setUsuario(usuario);

        when(repositorioObjetivo.buscarObjetivo(objetivoId)).thenReturn(objetivo);

        // Ejecución
        servicioObjetivo.actualizarObjetivo(objetivoId, montoAAgregar, userId);

        // Verificación
        assertEquals(500.0, objetivo.getMontoActual());
        assertEquals(500.0, usuario.getSaldo());
        verify(repositorioObjetivo).guardar(objetivo);
        verify(repositorioUsuario).modificar(usuario);
        verify(servicioTransaccion).registrarTransaccionSinActualizarSaldo(any(Egreso.class));
    }

    @Test
    void actualizarObjetivo_cuandoElSaldoEsInsuficiente_deberiaLanzarExcepcion() {
        // Preparación
        Integer objetivoId = 1;
        Long userId = 1L;
        Double montoAAgregar = 1500.0;

        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setSaldo(1000.0);

        Objetivo objetivo = new Objetivo();
        objetivo.setId(objetivoId);
        objetivo.setNombre("Viaje");
        objetivo.setMontoActual(0.0);
        objetivo.setUsuario(usuario);

        when(repositorioObjetivo.buscarObjetivo(objetivoId)).thenReturn(objetivo);

        // Ejecución y Verificación
        assertThrows(SaldoInsuficiente.class, () ->
                servicioObjetivo.actualizarObjetivo(objetivoId, montoAAgregar, userId)
        );
    }

    @Test
    void consultarObjetivo_cuandoElObjetivoExiste_deberiaRetornarObjetivo() {
        // Preparación
        Integer objetivoId = 1;
        Objetivo objetoEsperado = new Objetivo();
        objetoEsperado.setId(objetivoId);
        objetoEsperado.setNombre("Viaje");

        when(repositorioObjetivo.buscarObjetivo(objetivoId)).thenReturn(objetoEsperado);

        // Ejecución
        Objetivo objetoRecuperado = servicioObjetivo.consultarObjetivo(objetivoId);

        // Verificación
        assertNotNull(objetoRecuperado);
        assertEquals(objetivoId, objetoRecuperado.getId());
        assertEquals("Viaje", objetoRecuperado.getNombre());
    }

    @Test
    void consultarObjetivo_cuandoElObjetivoNoExiste_deberiaLanzarExcepcion() {
        // Preparación
        Integer objetivoId = 1;

        when(repositorioObjetivo.buscarObjetivo(objetivoId)).thenReturn(null);

        // Ejecución y Verificación
        assertThrows(IllegalArgumentException.class, () ->
                servicioObjetivo.consultarObjetivo(objetivoId)
        );
    }

    @Test
    void eliminarObjetivo_cuandoElObjetivoExiste_deberiaEliminarObjetivo() {
        // Preparación
        Integer objetivoId = 1;

        Usuario usuario = new Usuario();
        Objetivo objetivo = new Objetivo();
        objetivo.setId(objetivoId);
        objetivo.setUsuario(usuario);

        when(repositorioObjetivo.buscarObjetivo(objetivoId)).thenReturn(objetivo);

        // Ejecución
        servicioObjetivo.eliminarObjetivo(objetivoId);

        // Verificación
        verify(repositorioObjetivo).eliminarObjetivo(objetivoId);
    }

    @Test
    void eliminarObjetivo_cuandoElObjetivoNoExiste_deberiaLanzarExcepcion() {
        // Preparación
        Integer objetivoId = 1;

        when(repositorioObjetivo.buscarObjetivo(objetivoId)).thenReturn(null);

        // Ejecución y Verificación
        assertThrows(IllegalArgumentException.class, () ->
                servicioObjetivo.eliminarObjetivo(objetivoId)
        );
    }

    @Test
    void obtenerTodosLosObjetivos_deberiaRetornarListaDeObjetivos() {
        // Preparación
        List<Objetivo> objetivosEsperados = Collections.singletonList(new Objetivo());

        when(repositorioObjetivo.obtenerTodosLosObjetivos()).thenReturn(objetivosEsperados);

        // Ejecución
        List<Objetivo> objetivosRecuperados = servicioObjetivo.obtenerTodosLosObjetivos();

        // Verificación
        assertFalse(objetivosRecuperados.isEmpty());
        assertEquals(objetivosEsperados, objetivosRecuperados);
    }

    @Test
    void obtenerTodosLosObjetivosPorUsuario_cuandoElUsuarioExiste_deberiaRetornarObjetivos() {
        // Preparación
        Long userId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(userId);

        List<Objetivo> objetivosEsperados = Collections.singletonList(new Objetivo());

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(usuario);
        when(repositorioObjetivo.obtenerTodosLosObjetivosPorUsuario(userId)).thenReturn(objetivosEsperados);

        // Ejecución
        List<Objetivo> objetivosRecuperados = servicioObjetivo.obtenerTodosLosObjetivosPorUsuario(userId);

        // Verificación
        assertFalse(objetivosRecuperados.isEmpty());
        assertEquals(objetivosEsperados, objetivosRecuperados);
    }

    @Test
    void obtenerTodosLosObjetivosPorUsuario_cuandoElUsuarioNoExiste_deberiaLanzarExcepcion() {
        // Preparación
        Long userId = 1L;

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(null);

        // Ejecución y Verificación
        assertThrows(IllegalArgumentException.class, () ->
                servicioObjetivo.obtenerTodosLosObjetivosPorUsuario(userId)
        );
    }
}
