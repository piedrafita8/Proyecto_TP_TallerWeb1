/*package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoEgreso;
import com.tallerwebi.dominio.enums.TipoIngreso;
import com.tallerwebi.dominio.interfaces.ServicioTransaccion;
import com.tallerwebi.dominio.models.Transaccion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.test.annotation.Rollback;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.models.Ingreso;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDate;
import java.util.Collections;


public class ControladorTransaccionTest {

    private ControladorTransaccion controladorTransaccion;

    @Mock
    private ServicioTransaccion servicioTransaccion;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controladorTransaccion = new ControladorTransaccion(servicioTransaccion);
    }

    @Test
    @Rollback
    @Transactional
    void testMostrarTransaccion() {
        Model model = mock(Model.class);
    
        String viewName = controladorTransaccion.mostrarTransaccion(model);
    
        // Verificar que se agregó el atributo correcto al modelo
        verify(model).addAttribute(eq("datosTransaccion"), any(DatosTransaccion.class));
    
        // Verificar nombre de la vista
        assertThat(viewName, equalToIgnoringCase("transaccion"));
    }

    @Test
    @Rollback
    @Transactional
void testVerTransaccionesUsuarioAutenticado() {
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("id")).thenReturn(1L);
    when(servicioTransaccion.getTransaccionPorUserId(1L)).thenReturn(Collections.emptyList());

    ModelAndView result = controladorTransaccion.verTransacciones(request);

    assertThat(result.getViewName(), equalToIgnoringCase("index"));
    assertThat(result.getModel().containsKey("datosTransaccion"), is(true));
}

@Test
@Rollback
@Transactional
void testVerTransaccionesUsuarioNoAutenticado() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("id")).thenReturn(null); // Usuario no autenticado

    ModelAndView result = controladorTransaccion.verTransacciones(request);

    // Verifica que se haya agregado el error 
    assertThat(result.getModel().get("error").toString(), equalToIgnoringCase("No se pudo identificar al usuario."));
    assertThat(result.getViewName(), equalToIgnoringCase("index"));
}

@Test
@Rollback
@Transactional
void testCrearEgresoValido() {
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("id")).thenReturn(1L);

    ModelAndView result = controladorTransaccion.crearEgreso(
            100.0, LocalDate.now(), "Compra de insumos", TipoEgreso.SUPERMERCADO, request);

    verify(servicioTransaccion, times(1)).crearTransaccion(any(Egreso.class), eq(1L));
    assertThat(result.getViewName(), equalToIgnoringCase("redirect:/transaccion/gastos"));
}

@Test
@Rollback
@Transactional
void testCrearEgresoMontoInvalido() {
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpSession session = mock(HttpSession.class);
  
      when(request.getSession()).thenReturn(session);
      when(session.getAttribute("id")).thenReturn(1L); // Usuario autenticado
  
      ModelAndView result = controladorTransaccion.crearEgreso(
              -100.0,  // Monto inválido
              LocalDate.now(),
              "Descripción válida",
              TipoEgreso.SUPERMERCADO,
              request
      );
  
      // Verifica el mensaje de error
      assertThat(result.getModel().get("error").toString(), equalToIgnoringCase("El monto no puede ser nulo o menor a cero"));
}

@Test
@Rollback
@Transactional
void testCrearEgresoSaldoInsuficiente() {
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("id")).thenReturn(1L);
    doThrow(SaldoInsuficiente.class).when(servicioTransaccion).crearTransaccion(any(Egreso.class), eq(1L));

    ModelAndView result = controladorTransaccion.crearEgreso(
            100.0, LocalDate.now(), "Compra de insumos", TipoEgreso.SUPERMERCADO, request);

    assertThat(result.getViewName(), equalToIgnoringCase("gastos"));
   // assertThat(result.getModel().get("error"), equalToIgnoringCase("Saldo insuficiente para realizar el egreso."));
}

@Test
@Rollback
@Transactional
void testCrearIngresoValido() {
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("id")).thenReturn(1L);

    ModelAndView result = controladorTransaccion.crearIngreso(
            200.0, LocalDate.now(), "Pago mensual", TipoIngreso.SALARIO, request);

    verify(servicioTransaccion, times(1)).crearTransaccion(any(Ingreso.class), eq(1L));
    assertThat(result.getViewName(), equalToIgnoringCase("redirect:/transaccion/ingreso"));
}

@Test
@Rollback
@Transactional
void testVerDetalleTransaccionEncontrada() throws RecursoNoEncontrado {
    Transaccion transaccion = new Transaccion();
    when(servicioTransaccion.consultarTransaccion(100.0, 1)).thenReturn(transaccion);

    ModelAndView result = controladorTransaccion.verDetalleTransaccion(100.0, 1);

    assertThat(result.getViewName(), equalToIgnoringCase("detalleTransaccion"));
    assertThat(result.getModel().get("datosTransaccion"), is(transaccion));
}

@Test
@Rollback
@Transactional
void testTransaccionNoEncontrada() throws RecursoNoEncontrado {
  
    when(servicioTransaccion.consultarTransaccion(anyDouble(), anyInt()))
            .thenThrow(new RecursoNoEncontrado("Transaccion no encontrada"));


    Exception exception = assertThrows(RecursoNoEncontrado.class, () -> {
        controladorTransaccion.verDetalleTransaccion(100.0, 1);
    });

    // Verificar el mensaje de la excepción
    assertThat(exception.getMessage(), equalToIgnoringCase("Transaccion no encontrada"));
}





}
*/