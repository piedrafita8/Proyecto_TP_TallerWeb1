package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import com.tallerwebi.punta_a_punta.vistas.VistaObjetivo;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaObjetivoE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaObjetivo vistaObjetivo;
    VistaLogin vistaLogin;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        //browser = playwright.chromium().launch();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));

    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        Page page = context.newPage();
        vistaObjetivo = new VistaObjetivo(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaNavegarALaVistaObjetivoSiElUsuarioAgregaUnObjetivoNuevo() {
        vistaLogin.escribirUser("pepe");
        vistaLogin.escribirClave("test");
        vistaLogin.darClickEnIniciarSesion();
        String urlUsuario = vistaLogin.obtenerURLActual();
        assertThat(urlUsuario, containsStringIgnoringCase("/spring/index"));

        vistaObjetivo.escribirNombreDelObjetivo("Nuevo Auto");
        vistaObjetivo.escribirElMonto(250000.0);
        vistaObjetivo.escribirLaFechaObjetivo(LocalDate.of(2024, 10, 21));
        vistaObjetivo.darClickEnElBotonParaAgregar();
        String urlObjetivo = vistaObjetivo.obtenerURLActual();
        assertThat(urlObjetivo, containsStringIgnoringCase("/spring/objetivos"));
    }

    @Test
    void deberiaDecirFinanzasSATEnElNavbar() {
        String texto = vistaObjetivo.obtenerTextoDeLaBarraDeNavegacionEsquema();
        assertThat("Finanzas SAT", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaDecirObjetivosDeAhorroEnLaVistaObjetivos() {
        String texto = vistaObjetivo.obtenerTextoDeLaVistaObjetivos();
        assertThat("Objetivos de Ahorro", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaDeEliminarElObjetivoAlTocarElBotonParaEliminar() {
        vistaObjetivo.darClickEnElBotonParaEliminar();
        String url = vistaObjetivo.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/objetivos"));
    }
}
