package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaEgreso;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaEgresoE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaEgreso vistaEgreso;

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
        vistaEgreso = new VistaEgreso(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaDecirFinanzasSATEnElNavbar() {
        String texto = vistaEgreso.obtenerTextoDeLaBarraDeNavegacionEsquema();
        assertThat("Finanzas SAT", equalToIgnoringCase(texto));
    }

    // Revisar
    @Test
    void deberiaDarUnErrorAlCompletarElEgresoSinAntesIngresarDineroYTocarElBoton() {
        vistaEgreso.escribirElMonto(60000.0);
        vistaEgreso.darClickEnLaCategoria();
        vistaEgreso.escribirLaFecha(LocalDate.of(2024, 10, 21));
        vistaEgreso.escribirComentario("Gastos para almacen");
        String texto = vistaEgreso.obtenerMensajeDeError();
        assertThat("Saldo insuficiente para realizar el egreso.", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaNavegarAlGastoSiElUsuarioIngresaElMovimiento() {
        vistaEgreso.escribirElMonto(120000.0);
        vistaEgreso.darClickEnLaCategoria();
        vistaEgreso.escribirLaFecha(LocalDate.of(2024, 10, 21));
        vistaEgreso.escribirComentario("Gastos de alquiler");
        String url = vistaEgreso.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/gastos"));
    }
}
