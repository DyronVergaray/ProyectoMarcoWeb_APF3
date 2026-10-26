package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Anuncio de trabajo que publica una empresa.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): El anuncio con sus condiciones y su vigencia. Su estado (Activa,
 *    En Proceso, Finalizada) son las fases del anuncio: las cambia solo el administrador y solo en
 *    Activa se puede postular.
 *  - RF-05 (Filtros para búsqueda de trabajo): Sus datos (puesto, sueldo, frecuencia de pagos,
 *    horas, bonos, seguro de vida, estado y empresa) son los que el postulante usa para filtrar.
 */
@Entity
@Table(name = "Oferta")
public class Oferta {

    // Estados de una oferta: solo el administrador de la empresa los cambia, a mano.
    // Únicamente en "Activa" se puede postular.
    public static final String ACTIVA = "Activa";
    public static final String EN_PROCESO = "En Proceso";
    public static final String FINALIZADA = "Finalizada";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Oferta")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "Puesto", nullable = false, length = 100)
    private String puesto;

    @Column(name = "Sueldo", nullable = false, precision = 10, scale = 2)
    private BigDecimal sueldo;

    @Column(name = "Horas", nullable = false)
    private Integer horas;

    @Column(name = "Frecuencia_Pagos", nullable = false, length = 50)
    private String frecuenciaPagos;

    @Column(name = "Bonos", nullable = false, precision = 10, scale = 2)
    private BigDecimal bonos;

    @Column(name = "Gratificaciones", nullable = false, length = 100)
    private String gratificaciones;

    @Column(name = "Seguro_Vida", nullable = false, length = 50)
    private String seguroVida;

    @Column(name = "Vacaciones", nullable = false)
    private Integer vacaciones;

    @Column(name = "Otros", nullable = false, length = 255)
    private String otros;

    @Column(name = "Estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "Fecha_Inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "Fecha_Fin", nullable = false)
    private LocalDate fechaFin;

    public Oferta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public String getFrecuenciaPagos() {
        return frecuenciaPagos;
    }

    public void setFrecuenciaPagos(String frecuenciaPagos) {
        this.frecuenciaPagos = frecuenciaPagos;
    }

    public BigDecimal getBonos() {
        return bonos;
    }

    public void setBonos(BigDecimal bonos) {
        this.bonos = bonos;
    }

    public String getGratificaciones() {
        return gratificaciones;
    }

    public void setGratificaciones(String gratificaciones) {
        this.gratificaciones = gratificaciones;
    }

    public String getSeguroVida() {
        return seguroVida;
    }

    public void setSeguroVida(String seguroVida) {
        this.seguroVida = seguroVida;
    }

    public Integer getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(Integer vacaciones) {
        this.vacaciones = vacaciones;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
