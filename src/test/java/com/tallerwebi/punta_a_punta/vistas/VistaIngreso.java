package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

import java.time.LocalDate;

public class VistaIngreso extends VistaWeb{

    public VistaIngreso(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/ingreso");
    }

    public String obtenerTextoDeLaBarraDeNavegacionEsquema(){
        return this.obtenerTextoDelElemento(".nav-container .nav1 .navbar h1");
    }

    public String obtenerMensajeDeError(){
        return this.obtenerTextoDelElemento("p.alert.alert-danger");
    }

    public void escribirElMonto(Double monto){
        this.escribirEnElElementoNumero("#monto", monto);
    }

    public void darClickEnLaCategoria(){
        this.darClickEnElElemento("#categorias");
    }

    public void escribirLaFecha(LocalDate fecha){
        this.escribirEnElElementoFecha("#fecha", fecha);
    }

    public void escribirComentario(String comentario){
        this.escribirEnElElemento("#comentario", comentario);
    }
}