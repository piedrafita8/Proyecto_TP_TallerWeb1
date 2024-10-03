package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import com.tallerwebi.dominio.models.Ingreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorIngresoPresTest {

	private ControladorIngreso controladorIngreso;
	private Ingreso ingresoMock;
	private DatosIngreso datosIngresoMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioIngreso ServicioIngresoMock;

	@BeforeEach
	public void init(){
		datosIngresoMock = new DatosIngreso(450000.0, "Ingreso proveniente de mi sueldo", 1102024);
		ingresoMock = mock(Ingreso.class);
		when(ingresoMock.getDescripcion()).thenReturn("Ingreso proveniente de mi sueldo");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		ServicioIngresoMock = mock(ServicioIngreso.class);
		controladorIngreso = new ControladorIngreso(ServicioIngresoMock);
	}

	@Test
	public void ingresoSinDescripcionySinMontoDeberiaInsistirEnCompletarLaInformacion() {
		// Preparación
		DatosIngreso datosIngresoInvalido = new DatosIngreso(null, null, 0); // Sin descripción ni monto

		when(requestMock.getSession()).thenReturn(sessionMock);

		// Ejecución
		ModelAndView modelAndView = controladorIngreso.validarIngreso(datosIngresoInvalido, requestMock);

		// Validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/ingreso"));
		verify(sessionMock, times(1)).setAttribute("error", "Por favor, completa la información del ingreso.");
	}
	
	@Test
	public void ingresoConMontoYDescripcionCorrectosDeberiaLLevarAEsquema(){
		// preparacion
		Ingreso ingresoEncontradoMock = mock(Ingreso.class);
		when(ingresoEncontradoMock.getDescripcion()).thenReturn("Ingreso proveniente de mi sueldo");

		when(requestMock.getSession()).thenReturn(sessionMock);
		when(ServicioIngresoMock.consultarIngreso(450000.0, 1102024)).thenReturn(ingresoEncontradoMock);
		
		// ejecucion
		ModelAndView modelAndView = controladorIngreso.validarIngreso(datosIngresoMock, requestMock);
		
		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/esquema"));
		verify(sessionMock, times(1)).setAttribute("Ingreso proveniente de mi sueldo", ingresoEncontradoMock.getDescripcion());
	}
}
