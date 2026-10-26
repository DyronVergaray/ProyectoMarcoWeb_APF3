package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * El postulante: persona que busca trabajo.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Guarda los datos básicos con los que se registra (nombres,
 *    correo, DNI, teléfono, nivel educativo y descripción). No tiene fila en Rol: su rol de
 *    postulante es implícito.
 *  - RF-06 (Personalizar perfiles): Teléfono, nivel educativo y descripción son los datos que
 *    personaliza en su perfil; de él cuelgan sus títulos y su experiencia laboral.
 */
@Entity
@Table(name = "Estudiante")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Estudiante")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_PerfilEstudiante", nullable = false, unique = true)
    private PerfilEstudiante perfilEstudiante;

    @Column(name = "Nombre_Estudiante", nullable = false, length = 100)
    private String nombreEstudiante;

    @Column(name = "ApellidoP_Estudiante", nullable = false, length = 100)
    private String apellidoPEstudiante;

    @Column(name = "ApellidoM_Estudiante", nullable = false, length = 100)
    private String apellidoMEstudiante;

    @Column(name = "Correo", nullable = false, length = 150)
    private String correo;

    @Column(name = "Telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "DNI", nullable = false, length = 8)
    private String dni;

    @Column(name = "Descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "Nivel_Educativo", nullable = false, length = 100)
    private String nivelEducativo;

    public Estudiante() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PerfilEstudiante getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(PerfilEstudiante perfilEstudiante) {
        this.perfilEstudiante = perfilEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getApellidoPEstudiante() {
        return apellidoPEstudiante;
    }

    public void setApellidoPEstudiante(String apellidoPEstudiante) {
        this.apellidoPEstudiante = apellidoPEstudiante;
    }

    public String getApellidoMEstudiante() {
        return apellidoMEstudiante;
    }

    public void setApellidoMEstudiante(String apellidoMEstudiante) {
        this.apellidoMEstudiante = apellidoMEstudiante;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }
}
