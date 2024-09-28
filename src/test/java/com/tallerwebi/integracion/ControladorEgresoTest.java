package com.tallerwebi.integracion;

import com.tallerwebi.dominio.models.Egreso;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
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

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ControladorEgresoTest {

	private Egreso egresoMock;

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@BeforeEach
	public void init(){
		egresoMock = mock(Egreso.class);
		when(egresoMock.getMonto()).thenReturn(150.00);
		when(egresoMock.getDescripcion()).thenReturn("Compra de supermercado");

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void debeRedirigirALaPaginaDeEgresosCuandoSeNavegaALaRaiz() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/"))
				.andExpect(status().is3xxRedirection())
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assert modelAndView != null;
		assertThat("redirect:/egreso", equalToIgnoringCase(Objects.requireNonNull(modelAndView.getViewName())));
		assertThat(true, is(modelAndView.getModel().isEmpty()));
	}

	@Test
	public void debeRetornarLaPaginaEgresoConDatosCorrectos() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/egreso"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assert modelAndView != null;
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("egreso"));

		assertThat(modelAndView.getModel().get("datosEgreso").toString(), containsString("com.tallerwebi.presentacion.DatosEgreso"));
	}

	@Test
	public void debeContenerDescripcionYMontoCorrectosEnLaPaginaDeEgreso() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/egreso"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assert modelAndView != null;

		assertThat(modelAndView.getModel().get("montoEgreso").toString(), equalToIgnoringCase("15000.00"));
		assertThat(modelAndView.getModel().get("descripcionEgreso").toString(), containsString("Compra de materiales"));
	}
}
