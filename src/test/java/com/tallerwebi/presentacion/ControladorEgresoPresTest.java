package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.enums.TipoEgreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.models.Egreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorEgresoPresTest {

	private HttpSession sessionMock;
	private DatosEgreso datosEgresoMock;
	private ControladorEgreso controladorEgreso; // Cambiar el controlador a ControladorEgreso
	private Egreso egresoMock;                   // Mock del modelo Egreso
	private HttpServletRequest requestMock;
    private ServicioEgreso servicioEgresoMock;   // Mock del servicio de egreso

	@BeforeEach
	public void init(){
		// Inicializar el mock del modelo Egreso con datos de ejemplo
		datosEgresoMock = new DatosEgreso(32000.00, "Compra de insumos de oficina",LocalDate.of(2022, 12, 20));
		egresoMock = mock(Egreso.class);
		when(egresoMock.getMonto()).thenReturn(32000.00);
		when(egresoMock.getDescripcion()).thenReturn("Compra de insumos de oficina");

		// Inicializar los mocks de HttpServletRequest y HttpSession
		requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

		// Crear mock del servicio de egreso y del controlador
		servicioEgresoMock = mock(ServicioEgreso.class);
		controladorEgreso = new ControladorEgreso(servicioEgresoMock);
	}

	@Test
	public void egresoSinDescripcionAgregadoDebeMarcarComoError() {
		// Crear un objeto Egreso sin descripción
		//Egreso datosEgresoSinDescripcion = new Egreso(32000.00, "", 25102024);

		// Simular la obtención de la sesión a partir de la request
		when(requestMock.getSession()).thenReturn(sessionMock);

		// Llamar al metodo del controlador con el egreso sin descripción
		ModelAndView modelAndView = controladorEgreso.crearEgreso(23000.00,LocalDate.of(2022, 12, 20), "", TipoEgreso.SUPERMERCADO,requestMock );

		// Verificar que no se llame al servicio de crear egreso
		verify(servicioEgresoMock, never()).crearEgreso(any());

		// Verificar que el modelo contenga un mensaje de error
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("La descripción no puede estar vacía"));
	}



	@Test
	public void debeMostrarErrorSiNoEncuentraElEgreso() throws RecursoNoEncontrado {
		// Simular que el servicio lanza una excepción cuando no encuentra el egreso
		when(servicioEgresoMock.consultarEgreso(12345.00, 1)).thenThrow(new RecursoNoEncontrado("Egreso no encontrado"));

		// Llamar al metodo del controlador con un id de egreso inexistente
		ModelAndView modelAndView = controladorEgreso.verEgresos(1, requestMock);

		// Verificar que el controlador muestre la vista de error
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("gastos"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Egreso no encontrado"));
	}


	@Test
	public void egresoSinMontoAgregadoDebeMarcarComoError() {
		// Crear un objeto Egreso sin descripción


		// Simular la obtención de la sesión a partir de la request
		when(requestMock.getSession()).thenReturn(sessionMock);

		// Llamar al metodo del controlador con el egreso sin descripción
		ModelAndView modelAndView = controladorEgreso.crearEgreso(null, LocalDate.of(2022, 12, 20), "Compra de insumos de oficina", TipoEgreso.SUPERMERCADO ,requestMock);

		// Verificar que no se llame al servicio de crear egreso
		verify(servicioEgresoMock, never()).crearEgreso(any());

		// Verificar que el modelo contenga un mensaje de error
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El monto no puede ser nulo o menor a cero"));
	}
}
