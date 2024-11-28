package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.interfaces.ServicioDeuda;
import com.tallerwebi.dominio.interfaces.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ControladorDeudaTest {

    @Mock
    private ServicioDeuda servicioDeuda;

    @Mock
    private ServicioUsuario servicioUsuario;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ControladorDeuda controladorDeuda;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controladorDeuda).build();
    }

    

    @Test
    public void testEliminarDeuda() throws Exception {
        Long deudaId = 1L;
        doNothing().when(servicioDeuda).eliminarDeuda(deudaId);

        mockMvc.perform(delete("/deudas/{deudaId}", deudaId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deuda eliminada exitosamente."));
    }

    @Test
    public void testMarcarDeudaComoPagada() throws Exception {
        Long deudaId = 1L;
        doNothing().when(servicioDeuda).marcarDeudaComoPagada(deudaId);

        mockMvc.perform(put("/deudas/pagar/{deudaId}", deudaId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deuda marcada como pagada."));
    }
}