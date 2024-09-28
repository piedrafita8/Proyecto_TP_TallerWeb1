package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorEgresoTest {

	private ControladorEgreso controladorEgreso; // Cambiar el controlador a ControladorEgreso
	private Egreso egresoMock;                   // Mock del modelo Egreso
	private HttpServletRequest requestMock;
    private ServicioEgreso servicioEgresoMock;   // Mock del servicio de egreso


	@BeforeEach
	public void init(){
		// Inicializar el mock del modelo Egreso con datos de ejemplo
		egresoMock = mock(Egreso.class);
		when(egresoMock.getMonto()).thenReturn(150.00);
		when(egresoMock.getDescripcion()).thenReturn("Compra de insumos de oficina");

		// Inicializar los mocks de HttpServletRequest y HttpSession
		requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

		// Crear mock del servicio de egreso y del controlador
		servicioEgresoMock = mock(ServicioEgreso.class);
		controladorEgreso = new ControladorEgreso(servicioEgresoMock);
	}

	@Test
	public void debeMostrarLaListaDeEgresosCorrectamente() {
		// Simular el comportamiento del servicio para retornar una lista de egresos
		when(servicioEgresoMock.getAllEgresos()).thenReturn(List.of(egresoMock));

		// Ejecutar el metodo de mostrar lista de egresos
		ModelAndView modelAndView = controladorEgreso.verEgresos(requestMock);

		// Validar que la vista sea la esperada
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("lista-egresos"));

		// Validar que el modelo contiene los egresos correctamente
		assertThat(modelAndView.getModel().get("egresos").toString(), containsString("Compra de insumos de oficina"));
	}

	@Test
	public void debeMostrarErrorSiNoEncuentraElEgreso() throws RecursoNoEncontrado {
		// Simular una excepción RecursoNoEncontrado
		doThrow(new RecursoNoEncontrado("El egreso no existe")).when(servicioEgresoMock).consultarEgreso(150.00, 1);

		// Ejecutar el método de consultar un egreso inexistente
		ModelAndView modelAndView = controladorEgreso.verDetalleEgreso(150.00, 1, requestMock);

		// Validar que la vista sea la de error
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("error"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El egreso no existe"));
	}

	@Test
	public void registrarEgresoDebeRedirigirALaVistaDeConfirmacion() throws RecursoNoEncontrado {
		// Simular el registro de un egreso sin errores
		doNothing().when(servicioEgresoMock).registrar(any(Egreso.class));

		// Ejecutar el método de registrar el egreso
		ModelAndView modelAndView = controladorEgreso.registrarEgreso(egresoMock, requestMock);

		// Validar que la vista sea la de confirmación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("confirmacion"));
	}

	@Test
	public void errorAlRegistrarEgresoDebeMostrarMensajeDeError() throws RecursoNoEncontrado {
		// Simular un error al registrar un egreso
		doThrow(new RecursoNoEncontrado("Error al registrar el egreso")).when(servicioEgresoMock).registrar(any(Egreso.class));

		// Ejecutar el método de registrar el egreso
		ModelAndView modelAndView = controladorEgreso.registrarEgreso(egresoMock, requestMock);

		// Validar que la vista sea la de error con el mensaje adecuado
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("error"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el egreso"));
	}
}
