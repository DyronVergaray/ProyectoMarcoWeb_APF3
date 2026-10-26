package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Credencial de acceso del estudiante (su contraseña).
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Se crea al registrar al postulante y queda enlazado a su
 *    Estudiante.
 *  - RF-02 (Inicio de sesión): Su contraseña se compara al iniciar sesión.
 */
@Entity
@Table(name = "Perfil_Estudiante")
public class PerfilEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_PerfilEstudiante")
    private Long id;

    @Column(name = "Contrasena", nullable = false, length = 255)
    private String contrasena;

    public PerfilEstudiante() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
