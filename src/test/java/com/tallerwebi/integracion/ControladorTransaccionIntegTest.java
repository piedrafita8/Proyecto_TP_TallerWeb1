package com.tallerwebi.integracion;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ControladorTransaccionIntegTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        // Crear el contexto de pruebas
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void deberiaRetornarPaginaEgresoCuandoNavegaAEgreso() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/gastos"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("gastos"));
        assertThat(modelAndView.getModel().containsKey("datosTransaccion"), is(true)); // Verificar la clave directamente
    }

    @Test
    public void deberiaRetornarPaginaIngresoCuandoNavegaAIngreso() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/ingreso"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("ingreso"));
        assertThat(modelAndView.getModel().containsKey("datosTransaccion"), is(true)); // Verificar la clave directamente
    }

    @Test
    public void crearIngresoConMontoNuloODebajoDeCeroDebeRetornarError() throws Exception {
        // Enviar una solicitud POST con monto nulo
        MvcResult result = this.mockMvc.perform(post("/ingreso")
                        .param("monto", "0")
                        .param("fecha", "2022-12-20")
                        .param("descripcion", "Salario mensual")
                        .param("tipoIngreso", "SALARIO"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;

        // Verificar que se muestra el mensaje de error adecuado
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El monto no puede ser nulo o menor a cero"));
    }

    @Test
    public void crearIngresoSinDescripcionDebeRetornarError() throws Exception {
        // Enviar una solicitud POST con descripción vacía
        MvcResult result = this.mockMvc.perform(post("/ingreso")
                        .param("monto", "1500.00")
                        .param("fecha", "2022-12-20")
                        .param("descripcion", "")
                        .param("tipoIngreso", "SALARIO"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;

        // Verificar que se muestra el mensaje de error adecuado
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("La descripción no puede estar vacía"));
    }
}
