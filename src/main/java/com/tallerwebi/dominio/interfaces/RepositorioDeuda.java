package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.models.Deuda;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeuda {
    public void guardar(Deuda deuda);
    public void eliminar(Long id);
    public List<Deuda> obtenerDeudasPorUsuario(Long userId, boolean tipoDeudaEsDebo);
    public void marcarComoPagada(Long id);
}
