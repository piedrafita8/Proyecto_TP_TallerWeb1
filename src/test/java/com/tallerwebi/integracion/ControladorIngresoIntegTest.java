package com.tallerwebi.integracion;

import com.tallerwebi.dominio.interfaces.ServicioIngreso;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.DatosIngreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ControladorIngresoIntegTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	private ServicioIngreso servicioIngreso;
	private Usuario usuarioMock;

	@BeforeEach
	public void init() {
		usuarioMock = mock(Usuario.class);
		servicioIngreso = mock(ServicioIngreso.class);
		when(usuarioMock.getUsername()).thenReturn("damian");
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void debeRetornarLaPaginaIngresoCuandoSeNavegaALIngreso() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/ingreso"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assert modelAndView != null;
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("ingreso"));
		assertThat("El modelo debería contener la clave 'datosIngreso'", modelAndView.getModel().containsKey("datosIngreso"), is(true));
		assertThat(modelAndView.getModel().get("datosIngreso").toString(), containsString("com.tallerwebi.presentacion.DatosIngreso"));
	}

	@Test
	public void deberiaMostrarVistaIngresoConListaVaciaSiNoHayIngresos() throws Exception {
		// Configurar el servicio para devolver una lista vacía
		when(servicioIngreso.getAllIngresos()).thenReturn(Collections.emptyList());

		MvcResult result = this.mockMvc.perform(get("/ingreso"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assert modelAndView != null;
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("ingreso"));
		assertThat(modelAndView.getModel().containsKey("listaIngresos"), is(true));
		assertThat("La lista de ingresos debería estar vacía", ((List<?>) modelAndView.getModel().get("listaIngresos")).isEmpty(), is(true));
	}

//	// Descomentar este test cuando se cree los repositorios y controladores de esquema
//	@Test
//	public void deberiaRedirigirAEsquemaCuandoIngresoEsValido() throws Exception {
//		DatosIngreso datosIngreso = new DatosIngreso();
//		datosIngreso.setDescripcion("Salario del mes");
//		datosIngreso.setMonto(400000.0);
//		datosIngreso.setFecha(8102024);
//
//		// Simular que el ingreso existe en el servicio
//		when(servicioIngreso.consultarIngreso(datosIngreso.getMonto(), datosIngreso.getFecha()))
//				.thenReturn(new Ingreso(1, 400000.0, "Salario del mes", 8102024));
//
//		MvcResult result = this.mockMvc.perform(post("/validar-ingreso")
//						.flashAttr("datosIngreso", datosIngreso))
//				.andExpect(status().is3xxRedirection())
//				.andExpect(redirectedUrl("/esquema"))
//				.andReturn();
//
//		assertThat("Debería redirigir a la vista esquema", result.getResponse().getRedirectedUrl(), equalToIgnoringCase("/esquema"));
//	}

	@Test
	public void deberiaRedirigirAIngresoCuandoLosDatosSonInvalidos() throws Exception {
		DatosIngreso datosIngreso = new DatosIngreso();
		datosIngreso.setDescripcion(null); // Descripción nula para provocar el error
		datosIngreso.setMonto(-50.0); // Monto negativo, lo cual es inválido

		MvcResult result = this.mockMvc.perform(post("/validar-ingreso")
						.flashAttr("datosIngreso", datosIngreso))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/ingreso"))
				.andReturn();

		assertThat("Debería redirigir de nuevo a la vista de ingreso", result.getResponse().getRedirectedUrl(), equalToIgnoringCase("/ingreso"));
	}

	@Test
	public void deberiaAgregarMensajeDeErrorALaSesionCuandoIngresoEsInvalido() throws Exception {
		DatosIngreso datosIngreso = new DatosIngreso();
		datosIngreso.setDescripcion(null); // Provocar error con una descripción nula

		MvcResult result = this.mockMvc.perform(post("/validar-ingreso")
						.flashAttr("datosIngreso", datosIngreso))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/ingreso"))
				.andReturn();

		HttpSession session = result.getRequest().getSession();
        assert session != null;
        assertThat("La sesión debería contener un mensaje de error", session.getAttribute("error"), is("Por favor, completa la información del ingreso."));
	}

}
