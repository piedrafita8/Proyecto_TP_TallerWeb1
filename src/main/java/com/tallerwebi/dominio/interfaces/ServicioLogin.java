package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioLogin {

    Usuario consultarUsuario(String username, String password);
    void registrar(Usuario usuario) throws UsuarioExistente;

}
