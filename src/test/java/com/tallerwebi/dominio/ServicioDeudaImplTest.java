package com.tallerwebi.dominio;


import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioDeuda;
import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.servicios.ServicioDeudaImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ServicioDeudaImplTest {

    @Mock
    private RepositorioDeuda repositorioDeuda;

    @InjectMocks
    private ServicioDeudaImpl servicioDeuda;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void dadoQueExistenDeudasCuandoObtengoDeudasQueDeboEntoncesDevuelveSoloLasQueDebo() {
        
        Deuda deuda1 = new Deuda("Juan", 200.0, LocalDate.of(2024, 11, 25), TipoDeuda.DEBO, 1L);
        Deuda deuda2 = new Deuda("Carlos", 300.0, LocalDate.of(2024, 11, 26), TipoDeuda.ME_DEBEN, 1L);
        
        when(repositorioDeuda.obtenerDeudasPorUsuario(1L, true)).thenReturn(Arrays.asList(deuda1));
        
        List<Deuda> deudasDebo = servicioDeuda.obtenerDeudasQueDebo(1L);
        
        assertEquals(1, deudasDebo.size());
        assertEquals("Juan", deudasDebo.get(0).getNombre());
        verify(repositorioDeuda, times(1)).obtenerDeudasPorUsuario(1L, true);
    }

    @Test
    void dadoQueExistenDeudasCuandoObtengoDeudasQueMeDebenEntoncesDevuelveSoloLasQueMeDeben() {
      
        Deuda deuda1 = new Deuda("Juan", 200.0, LocalDate.of(2024, 11, 25), TipoDeuda.DEBO, 1L);
        Deuda deuda2 = new Deuda("Carlos", 300.0, LocalDate.of(2024, 11, 26), TipoDeuda.ME_DEBEN, 1L);

        when(repositorioDeuda.obtenerDeudasPorUsuario(1L, false)).thenReturn(Arrays.asList(deuda2));

        List<Deuda> deudasMeDeben = servicioDeuda.obtenerDeudasQueMeDeben(1L);

        assertEquals(1, deudasMeDeben.size());
        assertEquals("Carlos", deudasMeDeben.get(0).getNombre());
        verify(repositorioDeuda, times(1)).obtenerDeudasPorUsuario(1L, false);
    }

    @Test
    void dadoQueUnaDeudaNoExisteCuandoLaEliminoEntoncesLanzaExcepcion() {
        
        Deuda deuda = new Deuda("Juan", 200.0, LocalDate.of(2024, 11, 25), TipoDeuda.DEBO, 1L);

        when(repositorioDeuda.obtenerDeudasPorUsuario(1L, true)).thenReturn(Arrays.asList(deuda));
        doThrow(RecursoNoEncontrado.class).when(repositorioDeuda).eliminar(999L);

        assertThrows(RecursoNoEncontrado.class, () -> servicioDeuda.eliminarDeuda(999L));
    }

    @Test
    void dadoQueExistenDeudasCuandoMarcarComoPagadaEntoncesActualizaElEstadoCorrectamente() {

        Deuda deuda = new Deuda("Juan", 200.0, LocalDate.of(2024, 11, 25), TipoDeuda.DEBO, 1L);
        deuda.setId(1L);
        
        when(repositorioDeuda.obtenerDeudasPorUsuario(1L, true)).thenReturn(Arrays.asList(deuda));

        servicioDeuda.marcarDeudaComoPagada(1L);

        verify(repositorioDeuda, times(1)).marcarComoPagada(1L);
    }
}
