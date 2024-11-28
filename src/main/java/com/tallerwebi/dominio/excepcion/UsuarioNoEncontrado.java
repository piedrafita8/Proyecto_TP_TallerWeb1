package com.tallerwebi.dominio.excepcion;

public class UsuarioNoEncontrado extends RuntimeException{
    public UsuarioNoEncontrado(String message) {
        super(message);
    }
    
}
