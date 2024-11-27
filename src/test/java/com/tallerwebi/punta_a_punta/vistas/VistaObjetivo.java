package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

import java.time.LocalDate;

public class VistaObjetivo extends VistaWeb{

    public VistaObjetivo(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/objetivos");
    }

    public String obtenerTextoDeLaBarraDeNavegacionEsquema(){
        return this.obtenerTextoDelElemento(".nav-container .nav1 .navbar h1");
    }

    public String obtenerTextoDeLaVistaObjetivos(){
        return this.obtenerTextoDelElemento(".container h2");
    }

    public void escribirNombreDelObjetivo(String objetivo){
        this.escribirEnElElemento("#nombreObjetivo", objetivo);
    }

    public void escribirElMonto(Double monto){
        this.escribirEnElElementoNumero("#montoObjetivo", monto);
    }

    public void escribirLaFechaObjetivo(LocalDate fecha){
        this.escribirEnElElementoFecha("#fechaLimite", fecha);
    }

    public void darClickEnElBotonParaAgregar(){
        this.darClickEnElElemento("#botonObjetivo");
    }

    public void darClickEnElBotonParaEliminar(){
        this.darClickEnElElemento("#eliminarObjetivo");
    }
}