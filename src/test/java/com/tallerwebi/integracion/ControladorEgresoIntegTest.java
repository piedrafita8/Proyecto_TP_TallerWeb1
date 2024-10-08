package com.tallerwebi.integracion;

import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.DatosEgreso;
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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ControladorEgresoIntegTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	private ServicioEgreso servicioEgreso;
	private Usuario usuarioMock;

	@BeforeEach
	public void init() {
		usuarioMock = mock(Usuario.class);
		servicioEgreso = mock(ServicioEgreso.class);
		when(usuarioMock.getUsername()).thenReturn("alex");
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void debeRetornarLaPaginaEgresoCuandoSeNavegaALEgreso() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/egreso"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
		assertNotNull(modelAndView);
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("egreso"));
		assertThat("El modelo debería contener la clave 'datosEgreso'", modelAndView.getModel().containsKey("datosEgreso"), is(true));
		assertThat(modelAndView.getModel().get("datosEgreso"), instanceOf(DatosEgreso.class));
	}

	@Test
	public void deberiaMostrarVistaEgresoConListaVaciaSiNoHayEgreso() throws Exception {
		// Configurar el servicio para devolver una lista vacía
		when(servicioEgreso.getAllEgresos()).thenReturn(Collections.emptyList());

		MvcResult result;
        result = this.mockMvc.perform(get("/egreso"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
		assertNotNull(modelAndView);
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("egreso"));
		assertThat(modelAndView.getModel().containsKey("listaEgresos"), is(true));
		assertThat(modelAndView.getModel().get("listaEgresos"), instanceOf(List.class));
		assertThat("La lista de egresos debería estar vacía", ((List<?>) modelAndView.getModel().get("listaEgresos")).isEmpty(), is(true));
	}


	// Descomentar este test cuando se cree los repositorios y controladores de esquema
	/*
	@Test
	public void deberiaRedirigirAEsquemaCuandoEgresoEsValido() throws Exception {
		DatosEgreso datosEgreso = new DatosEgreso();
		datosEgreso.setDescripcion("Compra de insumos para oficina");
		datosEgreso.setMonto(30000.0);
		datosEgreso.setFecha(8102024);

		// Simular que el egreso existe en el servicio
		when(servicioEgreso.consultarEgreso(datosEgreso.getMonto(), datosEgreso.getFecha()))
				.thenReturn(new Egreso(1, 30000.0, "Compra de insumos para oficina", 8102024));

		MvcResult result = this.mockMvc.perform(post("/validar-egreso")
						.flashAttr("datosEgreso", datosEgreso))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/esquema"))
				.andReturn();

		assertThat("Debería redirigir a la vista esquema", result.getResponse().getRedirectedUrl(), equalToIgnoringCase("/esquema"));
	}
	*/
	@Test
	public void deberiaRedirigirAlEgresoCuandoLosDatosSonInvalidos() throws Exception {
		DatosEgreso datosEgreso = new DatosEgreso();
		datosEgreso.setDescripcion(null); // Descripción nula para provocar el error
		datosEgreso.setMonto(-50.0); // Monto negativo, lo cual es inválido

		MvcResult result = this.mockMvc.perform(post("/validar-egreso")
						.flashAttr("datosEgreso", datosEgreso))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/egreso"))
				.andReturn();

		assertThat("Debería redirigir de nuevo a la vista de egreso", result.getResponse().getRedirectedUrl(), equalToIgnoringCase("/egreso"));
	}

	@Test
	public void deberiaAgregarMensajeDeErrorALaSesionCuandoEgresoEsInvalido() throws Exception {
		DatosEgreso datosEgreso = new DatosEgreso();
		datosEgreso.setDescripcion(null); // Provocar error con una descripción nula

		MvcResult result = this.mockMvc.perform(post("/validar-egreso")
						.flashAttr("datosEgreso", datosEgreso))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/egreso"))
				.andReturn();

		HttpSession session = result.getRequest().getSession();
        assert session != null;
        assertThat("La sesión debería contener un mensaje de error", session.getAttribute("error"), is("Por favor, completa la información del egreso."));
	}
}