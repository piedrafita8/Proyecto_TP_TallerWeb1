package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
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
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioIngreso ServicioIngresoMock;

	@BeforeEach
	public void init(){
		ingresoMock = mock(Ingreso.class);
		when(ingresoMock.getDescripcion()).thenReturn("Ingreso proveniente de mi sueldo");
		when(ingresoMock.getMonto()).thenReturn(30500.00);
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		ServicioIngresoMock = mock(ServicioIngreso.class);
		controladorIngreso = new ControladorIngreso(ServicioIngresoMock);
	}

	@Test
	public void ingresoSinDatosDeberiaInsistirEnCompletarLaInformacion() throws RecursoNoEncontrado {
		// Preparación
		DatosIngreso datosIngresoInvalido = new DatosIngreso(null, null, null, null); // Sin descripción, monto ni fecha

		when(requestMock.getSession()).thenReturn(sessionMock);

		// Ejecución
		ModelAndView modelAndView = controladorIngreso.validarIngreso(datosIngresoInvalido, requestMock);

		// Validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/ingreso"));
		verify(sessionMock, times(1)).setAttribute("error", "Por favor, completa la información del ingreso.");
	}

	@Test
	public void debeMostrarErrorSiNoEncuentraElIngreso() throws RecursoNoEncontrado {
		// Simular que el servicio lanza una excepción cuando no encuentra el egreso
		when(ServicioIngresoMock.consultarIngreso(173345.00, 1)).thenThrow(new RecursoNoEncontrado("Ingreso no encontrado"));

		// Llamar al metodo del controlador con un id de egreso inexistente
		ModelAndView modelAndView = controladorIngreso.verIngresos(1, requestMock);

		// Verificar que el controlador muestre la vista de error
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("ingreso"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Ingreso no encontrado"));
	}
}
