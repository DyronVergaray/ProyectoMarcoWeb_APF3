package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Experiencia laboral de un estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Bloque Experiencia laboral del perfil del postulante: un
 *    estudiante puede tener muchas.
 *  - RF-04 (Anuncios de trabajo): La empresa la ve al revisar a un postulante.
 */
@Entity
@Table(name = "Experiencia_Laboral")
public class ExperienciaLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_ExperienciaLaboral")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Estudiante", nullable = false)
    private Estudiante estudiante;

    @Column(name = "Entidad", nullable = false, length = 150)
    private String entidad;

    @Column(name = "Nombre_Puesto", nullable = false, length = 100)
    private String nombrePuesto;

    @Column(name = "Periodo", nullable = false, length = 100)
    private String periodo;

    public ExperienciaLaboral() {
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

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
