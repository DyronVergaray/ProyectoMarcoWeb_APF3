package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Título académico de un estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Bloque Títulos del perfil del postulante: un estudiante puede
 *    tener muchos.
 *  - RF-04 (Anuncios de trabajo): La empresa lo ve al revisar a un postulante.
 */
@Entity
@Table(name = "Titulos")
public class Titulos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Titulos")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Estudiante", nullable = false)
    private Estudiante estudiante;

    @Column(name = "Entidad", nullable = false, length = 150)
    private String entidad;

    @Column(name = "Nombre_Titulo", nullable = false, length = 150)
    private String nombreTitulo;

    @Column(name = "Descargable", nullable = false, length = 255)
    private String descargable;

    public Titulos() {
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

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getNombreTitulo() {
        return nombreTitulo;
    }

    public void setNombreTitulo(String nombreTitulo) {
        this.nombreTitulo = nombreTitulo;
    }

    public String getDescargable() {
        return descargable;
    }

    public void setDescargable(String descargable) {
        this.descargable = descargable;
    }
}
