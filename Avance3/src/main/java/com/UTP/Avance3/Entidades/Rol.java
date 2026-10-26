package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Rol que puede tener un usuario de empresa.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Al registrar una empresa, su usuario recibe el rol Admin.
 *  - RF-03 (Control de acceso basado en roles): Es la base del control de acceso: un rol lo pueden
 *    tener muchos usuarios y cada usuario tiene uno. El nombre del rol decide qué funciones puede
 *    usar.
 */
@Entity
@Table(name = "Rol")
public class Rol {

    // Nombre del rol de administrador de empresa (se crea al arrancar la aplicación)
    public static final String ADMIN = "Admin";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Rol")
    private Long id;

    @Column(name = "Nombre_Rol", nullable = false, length = 50)
    private String nombreRol;

    public Rol() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}
