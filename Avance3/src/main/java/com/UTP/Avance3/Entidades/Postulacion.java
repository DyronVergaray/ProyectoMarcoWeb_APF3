package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Postulación de un estudiante a una oferta.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Registra quién postuló, a qué oferta, cuándo y en qué fase está
 *    (su estado). La empresa decide sobre ella y el postulante la ve en su historial.
 */
@Entity
@Table(name = "Postulacion")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Postulacion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Oferta", nullable = false)
    private Oferta oferta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_EstadoPostulacion", nullable = false)
    private EstadoPostulacion estadoPostulacion;

    @Column(name = "Fecha_Postulacion", nullable = false)
    private LocalDate fechaPostulacion;

    public Postulacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Oferta getOferta() {
        return oferta;
    }

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    public EstadoPostulacion getEstadoPostulacion() {
        return estadoPostulacion;
    }

    public void setEstadoPostulacion(EstadoPostulacion estadoPostulacion) {
        this.estadoPostulacion = estadoPostulacion;
    }

    public LocalDate getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(LocalDate fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }
}
