package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.models.Egreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorEgresoPresTest {

	private ControladorEgreso controladorEgreso;
	private Egreso egresoMock;
	private DatosEgreso datosEgresoMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioEgreso ServicioEgresoMock;

	@BeforeEach
	public void init(){
		datosEgresoMock = new DatosEgreso(30000.0, "Egreso para insumos de oficina", 19102024);
		egresoMock = mock(Egreso.class);
		when(egresoMock.getDescripcion()).thenReturn("Egreso para insumos de oficina");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		ServicioEgresoMock = mock(ServicioEgreso.class);
		controladorEgreso = new ControladorEgreso(ServicioEgresoMock);
	}

	@Test
	public void egresoSinDescripcionySinMontoDeberiaInsistirEnCompletarLaInformacion() {
		// Preparación
		DatosEgreso datosEgresoInvalido = new DatosEgreso(null, null, 5102024); // Sin descripción ni monto

		when(requestMock.getSession()).thenReturn(sessionMock);

		// Ejecución
		ModelAndView modelAndView = controladorEgreso.validarEgreso(datosEgresoInvalido, requestMock);

		// Validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/egreso"));
		verify(sessionMock, times(1)).setAttribute("error", "Por favor, completa la información del egreso.");
	}

	@Test
	public void egresoConMontoYDescripcionCorrectosDeberiaLLevarAEsquema(){
		// preparacion
		Egreso egresoEncontradoMock = mock(Egreso.class);
		when(egresoEncontradoMock.getDescripcion()).thenReturn("Egreso para insumos de oficina");

		when(requestMock.getSession()).thenReturn(sessionMock);
		when(ServicioEgresoMock.consultarEgreso(30000.0, 19102024)).thenReturn(egresoEncontradoMock);

		// ejecucion
		ModelAndView modelAndView = controladorEgreso.validarEgreso(datosEgresoMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/esquema"));
		verify(sessionMock, times(1)).setAttribute("Egreso para insumos de oficina", egresoEncontradoMock.getDescripcion());
	}
}
