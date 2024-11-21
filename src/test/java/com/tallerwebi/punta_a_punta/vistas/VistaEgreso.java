package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

import java.time.LocalDate;

public class VistaEgreso extends VistaWeb{

    public VistaEgreso(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/gastos");
    }

    public String obtenerTextoDeLaBarraDeNavegacionEsquema(){
        return this.obtenerTextoDelElemento("nav-container nav1 h1.navbar");
    }

    public String obtenerMensajeDeError(){
        return this.obtenerTextoDelElemento("p.alert.alert-danger");
    }

    public void darClickEnLaCategoria(){
        this.darClickEnElElemento("#categorias");
    }

    public void escribirElMonto(Double monto){
        this.escribirEnElElementoNumero("#monto", monto);
    }

    public void escribirLaFecha(LocalDate fecha){
        this.escribirEnElElementoFecha("#fecha", fecha);
    }

    public void escribirComentario(String comentario){
        this.escribirEnElElemento("#comentario", comentario);
    }

}
