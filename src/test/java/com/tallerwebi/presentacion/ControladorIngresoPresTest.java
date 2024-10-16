package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoEgreso;
import com.tallerwebi.dominio.enums.TipoIngreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import com.tallerwebi.dominio.models.Ingreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;

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
	public void egresoSinDescripcionAgregadoDebeMarcarComoError() {


		// Simular la obtención de la sesión a partir de la request
		when(requestMock.getSession()).thenReturn(sessionMock);

		// Llamar al metodo del controlador con el ingreso sin descripción
		ModelAndView modelAndView = controladorIngreso.crearIngreso(23000.00, LocalDate.of(2022, 12, 20), "", TipoIngreso.AHORROS,requestMock );

		// Verificar que no se llame al servicio de crear egreso
		verify(ServicioIngresoMock, never()).crearIngreso(any());

		// Verificar que el modelo contenga un mensaje de error
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("La descripción no puede estar vacía"));
	}


}
