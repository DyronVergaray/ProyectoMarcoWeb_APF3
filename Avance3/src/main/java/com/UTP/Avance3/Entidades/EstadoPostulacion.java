package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Fase de una postulación.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Activa (recién postulado), Aceptado o Rechazado. Los dos últimos
 *    son finales: no se pueden volver a cambiar.
 */
@Entity
@Table(name = "Estado_Postulacion")
public class EstadoPostulacion {

    // Estados de una postulación (se crean al arrancar la aplicación)
    public static final String ACTIVA = "Activa";
    public static final String RECHAZADO = "Rechazado";
    public static final String ACEPTADO = "Aceptado";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_EstadoPostulacion")
    private Long id;

    @Column(name = "Nombre_Estado", nullable = false, length = 50)
    private String nombreEstado;

    public EstadoPostulacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
}
