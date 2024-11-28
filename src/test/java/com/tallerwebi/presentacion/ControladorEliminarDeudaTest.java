package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.ServicioDeuda;
import com.tallerwebi.dominio.interfaces.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorEliminarDeudaTest {

    private ControladorDeuda controladorDeuda;
    private ServicioDeuda servicioDeuda;
    private ServicioUsuario servicioUsuario;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        // Creamos las dependencias mockeadas
        servicioDeuda = mock(ServicioDeuda.class);
        servicioUsuario = mock(ServicioUsuario.class);
        redirectAttributes = mock(RedirectAttributes.class);

        // Inicializamos el controlador
        controladorDeuda = new ControladorDeuda(servicioDeuda, servicioUsuario);
    }

    @Test
    public void testEliminarDeuda_deudaEliminadaExitosamente() throws Exception {
        Long deudaId = 1L;

        // Simulamos que el servicio elimina la deuda
        doNothing().when(servicioDeuda).eliminarDeuda(deudaId);

        // Llamamos al método en el controlador
        String viewName = controladorDeuda.eliminarDeuda(deudaId, redirectAttributes);

        // Verificamos la redirección
        assertThat(viewName, equalToIgnoringCase("redirect:/deudas"));

        // Verificamos que el mensaje flash sea agregado correctamente
        verify(redirectAttributes).addFlashAttribute("mensaje", "Deuda eliminada exitosamente.");
    }

    @Test
    public void testEliminarDeuda_deudaNoEncontrada() throws Exception {
        Long deudaId = 1L;

        // Simulamos que el servicio lanza una excepción RecursoNoEncontrado
        doThrow(new RecursoNoEncontrado("Deuda no encontrada")).when(servicioDeuda).eliminarDeuda(deudaId);

        // Llamamos al método en el controlador
        String viewName = controladorDeuda.eliminarDeuda(deudaId, redirectAttributes);

        // Verificamos la redirección
        assertThat(viewName, equalToIgnoringCase("redirect:/deudas"));

        // Verificamos que el mensaje de error sea agregado correctamente
        verify(redirectAttributes).addFlashAttribute("error", "Deuda no encontrada.");
    }

    @Test
    public void testEliminarDeuda_errorGenerico() throws Exception {
        Long deudaId = 1L;

        // Simulamos que el servicio lanza una excepción genérica
        doThrow(new RuntimeException("Error desconocido")).when(servicioDeuda).eliminarDeuda(deudaId);

        // Llamamos al método en el controlador
        String viewName = controladorDeuda.eliminarDeuda(deudaId, redirectAttributes);

        // Verificamos la redirección
        assertThat(viewName, equalToIgnoringCase("redirect:/deudas"));

        // Verificamos que el mensaje de error sea agregado correctamente
        verify(redirectAttributes).addFlashAttribute("error", "Error al eliminar la deuda.");
    }

    
}