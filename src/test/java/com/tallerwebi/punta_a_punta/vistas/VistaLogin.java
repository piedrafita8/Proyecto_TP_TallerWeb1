package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaLogin extends VistaWeb {

    public VistaLogin(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/login");
    }

    public String obtenerTextoDeLaBarraDeNavegacionLogin(){
        return this.obtenerTextoDelElemento("header h1.title a");
    }

    public String obtenerMensajeDeError(){
        return this.obtenerTextoDelElemento("p.alert.alert-danger");
    }

    public void escribirUser(String usuario){
        this.escribirEnElElemento("#username", usuario);
    }

    public void escribirEMAIL(String email){
        this.escribirEnElElemento("#email", email);
    }

    public void escribirClave(String clave){
        this.escribirEnElElemento("#password", clave);
    }

    public void darClickEnIniciarSesion(){
        this.darClickEnElElemento("#btn-login");
    }
}
