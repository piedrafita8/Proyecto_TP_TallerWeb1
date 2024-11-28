package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.models.Deuda;
import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.enums.TipoDeuda;
import com.tallerwebi.dominio.excepcion.RecursoNoEncontrado;
import com.tallerwebi.dominio.interfaces.ServicioDeuda;
import com.tallerwebi.dominio.interfaces.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ControladorDeudaTest {

    private ControladorDeuda controladorDeuda;

    private ServicioDeuda servicioDeuda;
    private ServicioUsuario servicioUsuario;

    private HttpServletRequest request;
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        servicioDeuda = mock(ServicioDeuda.class);
        servicioUsuario = mock(ServicioUsuario.class);

        controladorDeuda = new ControladorDeuda(servicioDeuda, servicioUsuario);

        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testMostrarDeudas_usuarioNoAutenticado() throws Exception {
        when(session.getAttribute("id")).thenReturn(null);

        String viewName = controladorDeuda.mostrarDeudas(request, mock(org.springframework.ui.Model.class));
        assertThat(viewName, equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void testMostrarDeudas_usuarioAutenticado() throws Exception {
        Long userId = 1L;
        when(session.getAttribute("id")).thenReturn(userId);

        Usuario usuario=new Usuario();
        List<Deuda> deudas = List.of(new Deuda("Deuda de prueba", 100.0, LocalDate.now(), TipoDeuda.DEBO, "Otra Persona", usuario));
        when(servicioDeuda.obtenerDeudasQueDebo(userId)).thenReturn(deudas);
        when(servicioDeuda.obtenerDeudasQueMeDeben(userId)).thenReturn(deudas);

        org.springframework.ui.Model model = mock(org.springframework.ui.Model.class);

        String viewName = controladorDeuda.mostrarDeudas(request, model);
        assertThat(viewName, equalToIgnoringCase("deudas"));
        verify(model).addAttribute("debo", deudas);
        verify(model).addAttribute("medeben", deudas);
    }

    @Test
    public void testAgregarDeuda_usuarioNoAutenticado() throws Exception {
        when(session.getAttribute("id")).thenReturn(null);

        String viewName = controladorDeuda.agregarDeuda(new Deuda(), request, mock(org.springframework.web.servlet.mvc.support.RedirectAttributes.class));
        assertThat(viewName, equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void testAgregarDeuda_usuarioAutenticado() throws Exception {
        Long userId = 1L;
        when(session.getAttribute("id")).thenReturn(userId);

        Usuario usuario = new Usuario();
        usuario.setId(userId);
        when(servicioUsuario.obtenerUsuarioPorId(userId)).thenReturn(usuario);

        Deuda deuda = new Deuda();
        deuda.setMonto(100.0);
        deuda.setDescripcion("Deuda de prueba");

        doNothing().when(servicioDeuda).agregarDeuda(any(Deuda.class));

        String viewName = controladorDeuda.agregarDeuda(deuda, request, mock(org.springframework.web.servlet.mvc.support.RedirectAttributes.class));
        assertThat(viewName, equalToIgnoringCase("redirect:/deudas"));
    }


    @Test
    public void testMarcarDeudaComoPagada() throws Exception {
        Long deudaId = 1L;

        doNothing().when(servicioDeuda).marcarDeudaComoPagada(deudaId);

        String response = controladorDeuda.marcarComoPagada(deudaId).getBody();
        assertThat(response, equalToIgnoringCase("Deuda marcada como pagada."));
    }
}