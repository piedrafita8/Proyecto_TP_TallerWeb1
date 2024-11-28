package com.tallerwebi.dominio;

import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioDeuda;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.servicios.ServicioDeudaImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioDeudaImplTest {

    private RepositorioDeuda repositorioDeuda;
    private RepositorioUsuario repositorioUsuario;
    private ServicioDeudaImpl servicioDeuda;

    @BeforeEach
    public void init() {
        repositorioDeuda = mock(RepositorioDeuda.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        servicioDeuda = new ServicioDeudaImpl(repositorioDeuda, repositorioUsuario);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoUnaDeudaCuandoSeAgregaEntoncesEsGuardadaEnElRepositorio() {
        
        Usuario usuario = new Usuario("usuarioTest", "test@mail.com", "password", "ROLE_USER");
        Deuda deuda = new Deuda("Préstamo", 100.0, LocalDate.now(), TipoDeuda.DEBO, "Persona1", usuario);

        servicioDeuda.agregarDeuda(deuda);

        verify(repositorioDeuda, times(1)).guardar(deuda);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoUnaDeudaCuandoSeEliminaEntoncesEsRemovidaDelRepositorio() {
        
        Long deudaId = 1L;
        Deuda deuda = new Deuda();
        when(repositorioDeuda.obtenerPorId(deudaId)).thenReturn(deuda);

        servicioDeuda.eliminarDeuda(deudaId);

        verify(repositorioDeuda, times(1)).eliminar(deudaId);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoUnIdInexistenteCuandoSeEliminaEntoncesLanzaExcepcion() {
        
        Long deudaId = 1L;
        when(repositorioDeuda.obtenerPorId(deudaId)).thenReturn(null);

        assertThrows(RecursoNoEncontrado.class, () -> servicioDeuda.eliminarDeuda(deudaId));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoUnaDeudaCuandoSeMarcaComoPagadaEntoncesElEstadoSeActualiza() {
        
        Long deudaId = 1L;
        Deuda deuda = new Deuda();
        when(repositorioDeuda.obtenerPorId(deudaId)).thenReturn(deuda);

        servicioDeuda.marcarDeudaComoPagada(deudaId);

        assertThat(deuda.isPagado(), is(true));
        verify(repositorioDeuda, times(1)).guardar(deuda);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoUnUsuarioConDeudasCuandoObtengoDeudasQueDeboEntoncesDevuelveSoloPendientes() {
       
        Long userId = 1L;
        Usuario usuario = new Usuario();
        List<Deuda> deudasPendientes = new ArrayList<>();
        deudasPendientes.add(new Deuda("Préstamo", 100.0, LocalDate.now(), TipoDeuda.DEBO, "Persona1", usuario));

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(usuario);
        when(repositorioDeuda.obtenerDeudasPorUsuario(usuario, TipoDeuda.DEBO)).thenReturn(deudasPendientes);

        List<Deuda> resultado = servicioDeuda.obtenerDeudasQueDebo(userId);

        assertThat(resultado, hasSize(1));
        assertThat(resultado.get(0).getTipoDeuda(), is(TipoDeuda.DEBO));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoUnUsuarioConDeudasCuandoObtengoDeudasQueMeDebenEntoncesDevuelveSoloPorCobrar() {
        
        Long userId = 1L;
        Usuario usuario = new Usuario();
        List<Deuda> deudasPorCobrar = new ArrayList<>();
        deudasPorCobrar.add(new Deuda("Cobro", 200.0, LocalDate.now(), TipoDeuda.ME_DEBEN, "Persona2", usuario));

        when(repositorioUsuario.buscarPorId(userId)).thenReturn(usuario);
        when(repositorioDeuda.obtenerDeudasPorUsuario(usuario, TipoDeuda.ME_DEBEN)).thenReturn(deudasPorCobrar);

        List<Deuda> resultado = servicioDeuda.obtenerDeudasQueMeDeben(userId);

        assertThat(resultado, hasSize(1));
        assertThat(resultado.get(0).getTipoDeuda(), is(TipoDeuda.ME_DEBEN));
    }

    @Test
    @Transactional
    @Rollback
    public void dadoUnUsuarioInexistenteCuandoObtengoDeudasEntoncesLanzaExcepcion() {
        
        Long userId = 1L;
        when(repositorioUsuario.buscarPorId(userId)).thenReturn(null);

        assertThrows(RecursoNoEncontrado.class, () -> servicioDeuda.obtenerDeudasQueDebo(userId));
        assertThrows(RecursoNoEncontrado.class, () -> servicioDeuda.obtenerDeudasQueMeDeben(userId));
    }
}