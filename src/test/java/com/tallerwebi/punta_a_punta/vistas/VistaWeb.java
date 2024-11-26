package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.time.LocalDate;

public class VistaWeb {
    protected Page page;

    public VistaWeb(Page page) {
        this.page = page;
    }

    public String obtenerURLActual(){
        return page.url();
    }

    protected String obtenerTextoDelElemento(String selectorCSS){
        return this.obtenerElemento(selectorCSS).textContent();
    }

    protected void darClickEnElElemento(String selectorCSS){
        this.obtenerElemento(selectorCSS).click();
    }

    protected void escribirEnElElemento(String selectorCSS, String texto){
        this.obtenerElemento(selectorCSS).type(texto);
    }

    protected void escribirEnElElementoNumero(String selectorCSS, Double monto){
        this.obtenerElemento(selectorCSS).type(String.valueOf(monto));
    }

    private Locator obtenerElemento(String selectorCSS){
        return page.locator(selectorCSS);
    }

    protected void escribirEnElElementoFecha(String selectorCSS, LocalDate fecha) {
        this.obtenerElemento(selectorCSS).type(String.valueOf(fecha));
    }
}
