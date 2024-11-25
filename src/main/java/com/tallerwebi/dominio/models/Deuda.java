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

    private String nombre;
    private Double monto;
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private TipoDeuda tipoDeuda; 
    private Long userId; 
    private boolean pagado; 

    public Deuda() {}

    public Deuda(String nombre, Double monto, LocalDate fecha, TipoDeuda tipoDeuda, Long userId) {
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
        this.tipoDeuda = tipoDeuda;
        this.userId = userId;
        this.pagado = false; 
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    // Getters y Setters
    
}