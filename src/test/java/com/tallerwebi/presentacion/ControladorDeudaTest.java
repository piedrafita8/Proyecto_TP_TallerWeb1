package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.interfaces.ServicioDeuda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
/*import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;

public class ControladorDeudaTest {

    @InjectMocks
    private ControladorDeuda controladorDeuda;

    @Mock
    private ServicioDeuda servicioDeuda;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodasLasDeudas() {
        
        List<Deuda> deudasQueDebo = Arrays.asList(new Deuda("Descripcion", 200.0, null, TipoDeuda.DEBO,"nombre", 1L));
        List<Deuda> deudasQueMeDeben = Arrays.asList(new Deuda("Descripcion", 200.0, null, TipoDeuda.ME_DEBEN,"nombre", 1L));
        
        when(servicioDeuda.obtenerDeudasQueDebo(1L)).thenReturn(deudasQueDebo);
        when(servicioDeuda.obtenerDeudasQueMeDeben(1L)).thenReturn(deudasQueMeDeben);

        Map<String, List<Deuda>> resultado = controladorDeuda.obtenerTodasLasDeudas(1L);

        // Verificar 
        assertThat(resultado.size(), is(2));
        assertThat(resultado.get("debo"), is(equalTo(deudasQueDebo)));
        assertThat(resultado.get("medeben"), is(equalTo(deudasQueMeDeben)));
    }

    @Test
    void testAgregarDeuda() {
       
        Deuda deuda = new Deuda("Descripcion", 200.0, null, TipoDeuda.DEBO,"nombre", 1L);

        controladorDeuda.agregarDeuda(deuda);

        verify(servicioDeuda).agregarDeuda(deuda);
    }

    @Test
    void testEliminarDeuda() {
        
        doNothing().when(servicioDeuda).eliminarDeuda(1L);

        controladorDeuda.eliminarDeuda(1L);

        verify(servicioDeuda).eliminarDeuda(1L);
    }

    @Test
    void testMarcarComoPagada() {
        
        doNothing().when(servicioDeuda).marcarDeudaComoPagada(1L);

        controladorDeuda.marcarComoPagada(1L);

        // Verificar
        verify(servicioDeuda).marcarDeudaComoPagada(1L);
    }
}*/