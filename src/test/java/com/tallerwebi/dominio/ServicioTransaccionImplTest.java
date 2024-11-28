package com.tallerwebi.dominio;
/*
import com.tallerwebi.dominio.enums.TipoIngreso;
import com.tallerwebi.dominio.enums.TipoMovimiento;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontrado;
import com.tallerwebi.dominio.interfaces.RepositorioTransaccion;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.models.Transaccion;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.servicios.ServicioTransaccionImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ServicioTransaccionImplTest {

    private RepositorioUsuario repositorioUsuario;
    private RepositorioTransaccion repositorioTransaccion;
    private ServicioTransaccionImpl servicioTransaccion;

    @BeforeEach
    public void setup() {
        repositorioUsuario = mock(RepositorioUsuario.class);
        repositorioTransaccion = mock(RepositorioTransaccion.class);
        servicioTransaccion = new ServicioTransaccionImpl(repositorioUsuario, repositorioTransaccion);
    }

    @Test
    @Rollback
    @Transactional
void registrarIngreso_saldoActualizadoCorrectamente() throws UsuarioNoEncontrado {
    Usuario usuario = new Usuario( "Usuario 1", "mail","contra","rol");
    Ingreso ingreso = new Ingreso(null, 500.0, "Salario", LocalDate.now(), usuario.getId(),TipoIngreso.AHORROS);

    when(repositorioUsuario.buscarPorId(usuario.getId())).thenReturn(usuario);

    servicioTransaccion.crearTransaccion(ingreso, usuario.getId());

    assertEquals(500.0, usuario.getSaldo());
    verify(repositorioUsuario).modificar(usuario);
    verify(repositorioTransaccion).guardar(ingreso);
}
@Test
@Rollback
@Transactional
void registrarEgreso_saldoActualizadoCorrectamente() throws UsuarioNoEncontrado {
    Usuario usuario = new Usuario("Usuario 1", "mail","contra","rol");
    Egreso egreso = new Egreso(null, 200.0, "Supermercado", LocalDate.now(), usuario.getId(), null);
    usuario.setSaldo(1000.0);
    when(repositorioUsuario.buscarPorId(usuario.getId())).thenReturn(usuario);

    servicioTransaccion.crearTransaccion(egreso, usuario.getId());

    assertEquals(800.0, usuario.getSaldo());
    verify(repositorioUsuario).modificar(usuario);
    verify(repositorioTransaccion).guardar(egreso);
}
@Test
@Rollback
@Transactional
void registrarEgreso_saldoInsuficiente_lanzaExcepcion() {
    Usuario usuario = new Usuario("Usuario 1", "mail","contra","rol");
    Egreso egreso = new Egreso(null, 200.0, "Supermercado", LocalDate.now(), usuario.getId(), null);

    when(repositorioUsuario.buscarPorId(usuario.getId())).thenReturn(usuario);

    SaldoInsuficiente exception = assertThrows(SaldoInsuficiente.class, () -> {
        servicioTransaccion.crearTransaccion(egreso, usuario.getId());
    });

    assertEquals("Saldo insuficiente para realizar la transacción.", exception.getMessage());
    verify(repositorioUsuario, never()).modificar(usuario);
    verify(repositorioTransaccion, never()).guardar(egreso);
}
@Test
@Rollback
@Transactional
void registrarTransaccion_usuarioNoEncontrado_lanzaExcepcion() {
    Long userId = 1L;
    Egreso egreso = new Egreso(null, 200.0, "Supermercado", LocalDate.now(), userId, null);

    when(repositorioUsuario.buscarPorId(userId)).thenReturn(null);

    UsuarioNoEncontrado exception = assertThrows(UsuarioNoEncontrado.class, () -> {
        servicioTransaccion.crearTransaccion(egreso, userId);
    });

    assertEquals("Usuario no encontrado con ID: " + userId, exception.getMessage());
    verify(repositorioUsuario, never()).modificar(any());
    verify(repositorioTransaccion, never()).guardar(egreso);
}
@Test
@Rollback
@Transactional
void obtenerTodasLasTransacciones_devuelveListaCorrecta() {
    List<Transaccion> transacciones = Arrays.asList(
            new Ingreso(1, 500.0, "Salario", LocalDate.now(), 1L, null),
            new Ingreso(2, 100.0, "Donación", LocalDate.now(), 1L, null)
    );

    when(repositorioTransaccion.obtener()).thenReturn(transacciones);

    List<Transaccion> resultado = servicioTransaccion.getAllTransacciones();

    assertEquals(2, resultado.size());
    assertEquals(transacciones, resultado);
}

@Test
@Rollback
@Transactional
void consultarTransaccion_transaccionEncontrada_devuelveTransaccion() throws RecursoNoEncontrado {

    Transaccion transaccionEsperada = new Ingreso(1, 500.0, "Salario", LocalDate.now(), 1L, null);

    when(repositorioTransaccion.buscar(500.0, 1)).thenReturn(transaccionEsperada);

    Transaccion resultado = servicioTransaccion.consultarTransaccion(500.0, 1);

    assertNotNull(resultado);
    assertEquals(transaccionEsperada, resultado);
    verify(repositorioTransaccion, times(1)).buscar(500.0, 1);
}
@Test
@Rollback
@Transactional
void consultarTransaccion_transaccionNoEncontrada_lanzaExcepcion() {
   
    when(repositorioTransaccion.buscar(500.0, 1)).thenReturn(null);

    RecursoNoEncontrado exception = assertThrows(RecursoNoEncontrado.class, () -> {
        servicioTransaccion.consultarTransaccion(500.0, 1);
    });

    assertEquals("Egreso no encontrado con monto: 500.0 e id: 1", exception.getMessage());
    verify(repositorioTransaccion, times(1)).buscar(500.0, 1);
}
@Test
@Rollback
@Transactional
void consultarTransaccion_montoNegativo_lanzaExcepcion() {
    when(repositorioTransaccion.buscar(500.0, 1)).thenReturn(null);

    RecursoNoEncontrado exception = assertThrows(RecursoNoEncontrado.class, () -> {
        servicioTransaccion.consultarTransaccion(500.0, 1);
    });

    assertEquals("Egreso no encontrado con monto: 500.0 e id: 1", exception.getMessage());

    verify(repositorioTransaccion, times(1)).buscar(500.0, 1);
}
@Test
@Rollback
@Transactional
void getTransaccionPorUserId_devuelveListaDeTransacciones() {
    Long userId = 1L;
    List<Transaccion> transacciones = Arrays.asList(
        new Ingreso(1, 500.0, "Salario", LocalDate.now(), userId, null),
        new Egreso(2, 100.0, "Supermercado", LocalDate.now(), userId, null)
    );

    when(repositorioTransaccion.buscarTransaccionPorUsuario(userId)).thenReturn(transacciones);

    List<Transaccion> resultado = servicioTransaccion.getTransaccionPorUserId(userId);

    assertEquals(2, resultado.size());
    assertEquals(transacciones, resultado);
}
@Test
@Rollback
@Transactional
void registrarTransaccionSinActualizarSaldo_guardaTransaccionSinActualizarSaldoDeUsuario() {
    Transaccion transaccion = new Transaccion(150.0, "Pago por servicios", LocalDate.now(), 1l);

    servicioTransaccion.registrarTransaccionSinActualizarSaldo(transaccion);

    verify(repositorioTransaccion, times(1)).guardar(transaccion);
    verifyNoInteractions(repositorioUsuario);
}

}*/