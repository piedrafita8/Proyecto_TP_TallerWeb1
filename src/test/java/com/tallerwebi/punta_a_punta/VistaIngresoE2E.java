package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaIngreso;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaIngresoE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaIngreso vistaIngreso;

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
        vistaIngreso = new VistaIngreso(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaDecirFinanzasSATEnElNavbar() {
        String texto = vistaIngreso.obtenerTextoDeLaBarraDeNavegacionEsquema();
        assertThat("Finanzas SAT", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaDecirINGRESOSEnLaVistaIngreso() {
        String texto = vistaIngreso.obtenerTextoDeLaVistaIngreso();
        assertThat("INGRESOS", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaNavegarAlGastoSiElUsuarioIngresaElMovimiento() {
        vistaIngreso.escribirElMonto(10000.0);
        vistaIngreso.darClickEnLaCategoria();
        vistaIngreso.escribirLaFecha(LocalDate.of(2024, 5, 29));
        vistaIngreso.escribirComentario("Ingreso de mi sueldo");
        String url = vistaIngreso.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/ingreso"));
    }
}
