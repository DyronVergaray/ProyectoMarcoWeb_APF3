package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Usuario de una empresa.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Se crea al registrar la empresa, con sus datos básicos.
 *  - RF-02 (Inicio de sesión): Está enlazado a sus Credenciales, que se validan al iniciar sesión.
 *  - RF-03 (Control de acceso basado en roles): Su Rol decide qué puede hacer, y su Empresa limita
 *    lo que puede ver: un usuario pertenece a una sola empresa y una empresa tiene muchos usuarios.
 */
@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Usuario")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Rol", nullable = false)
    private Rol rol;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Credenciales", nullable = false, unique = true)
    private Credenciales credenciales;

    @Column(name = "Nombre_Usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "ApellidoP_Usuario", nullable = false, length = 100)
    private String apellidoPUsuario;

    @Column(name = "ApellidoM_Usuario", nullable = false, length = 100)
    private String apellidoMUsuario;

    @Column(name = "Correo", nullable = false, length = 150)
    private String correo;

    @Column(name = "DNI", nullable = false, length = 8)
    private String dni;

    public Usuario() {
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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoPUsuario() {
        return apellidoPUsuario;
    }

    public void setApellidoPUsuario(String apellidoPUsuario) {
        this.apellidoPUsuario = apellidoPUsuario;
    }

    public String getApellidoMUsuario() {
        return apellidoMUsuario;
    }

    public void setApellidoMUsuario(String apellidoMUsuario) {
        this.apellidoMUsuario = apellidoMUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
