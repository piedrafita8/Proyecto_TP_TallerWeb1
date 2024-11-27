package com.tallerwebi.dominio.models;

import javax.persistence.*;

import com.tallerwebi.dominio.enums.TipoDeuda;

import java.time.LocalDate;

@Entity
@Table(name = "deudas")
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion; 
    private Double monto;
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private TipoDeuda tipoDeuda; // DEBO - ME_DEBEN

    private String otraPersona; // Nombre de la persona deudora o acreedora (manual)

    private boolean pagado = false;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Long usuarioId;

    public Deuda() {}

    public Deuda(String descripcion, Double monto, LocalDate fecha, TipoDeuda tipoDeuda, String nombrePersona, Long usuarioId) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.tipoDeuda = tipoDeuda;
        this.otraPersona = nombrePersona;
        this.usuarioId = usuarioId;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

   

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoDeuda getTipoDeuda() {
        return tipoDeuda;
    }

    public void setTipoDeuda(TipoDeuda tipoDeuda) {
        this.tipoDeuda = tipoDeuda;
    }

    public String getOtraPersona() {
        return otraPersona;
    }

    public void setOtraPersona(String otraPersona) {
        this.otraPersona = otraPersona;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Deuda other = (Deuda) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
    
}