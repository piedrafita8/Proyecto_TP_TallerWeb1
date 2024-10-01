package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import com.tallerwebi.dominio.interfaces.ServicioLogin;
import com.tallerwebi.dominio.models.Ingreso;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
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

//	@Test
//	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente(){
//		// preparacion
//		when(ServicioIngresoMock.consultarUsuario(anyString(), anyString())).thenReturn(null);
//
//		// ejecucion
//		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);
//
//		// validacion
//		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
//		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
//		verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
//	}
	
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

//	@Test
//	public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente {
//
//		// ejecucion
//		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);
//
//		// validacion
//		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
//		verify(ServicioLoginMock, times(1)).registrar(usuarioMock);
//	}

//	@Test
//	public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
//		// preparacion
//		doThrow(UsuarioExistente.class).when(ServicioLoginMock).registrar(usuarioMock);
//
//		// ejecucion
//		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);
//
//		// validacion
//		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
//		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe"));
//	}
//
//	@Test
//	public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
//		// preparacion
//		doThrow(RuntimeException.class).when(ServicioLoginMock).registrar(usuarioMock);
//
//		// ejecucion
//		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);
//
//		// validacion
//		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
//		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario"));
//	}
}
