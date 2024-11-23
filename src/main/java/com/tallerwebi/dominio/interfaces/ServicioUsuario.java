package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Usuario;

public interface ServicioUsuario {

    Usuario obtenerUsuarioPorId(Long id);
    void actualizarSaldo(Long id, Double nuevoSaldo);
}