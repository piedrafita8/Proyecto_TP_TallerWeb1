package com.tallerwebi.integracion;

import com.tallerwebi.dominio.interfaces.ServicioEgreso;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ControladorEgresoIntegTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	private ServicioEgreso servicioEgresoMock;

	@BeforeEach
	public void setup() {
		// Crear un mock del servicio de egresos
		servicioEgresoMock = Mockito.mock(ServicioEgreso.class);
		// Crear el contexto de pruebas
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void debeRedirigirALaPaginaDeEgresosCuandoSeNavegaALaRaiz() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/"))
				.andExpect(status().is3xxRedirection()) // Esperar redireccionamiento 3xx
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assert modelAndView != null;
		assertThat("redirect:/egreso", equalToIgnoringCase(Objects.requireNonNull(modelAndView.getViewName())));
		assertThat(true, is(modelAndView.getModel().isEmpty()));
	}

	@Test
	public void debeMostrarLaPaginaEgresoCuandoSeNavegaAEgreso() throws Exception {
		// Mock de datos para la lista de egresos
		Mockito.when(servicioEgresoMock.getAllEgresos()).thenReturn(Collections.emptyList());

		MvcResult result = this.mockMvc.perform(get("/egreso"))
				.andExpect(status().isOk()) // Esperar código de estado 200
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assert modelAndView != null;
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("egreso"));
		assertThat(modelAndView.getModel().get("datosEgreso").toString(), containsString("[]")); // Verificar lista vacía
	}
}
