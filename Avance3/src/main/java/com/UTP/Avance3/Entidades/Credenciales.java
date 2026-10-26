package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Credencial de acceso del usuario de empresa (su contraseña).
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Se crea al registrar la empresa y queda enlazada a su usuario
 *    administrador.
 *  - RF-02 (Inicio de sesión): Su contraseña se compara al iniciar sesión.
 */
@Entity
@Table(name = "Credenciales")
public class Credenciales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Credenciales")
    private Long id;

    @Column(name = "Contrasena", nullable = false, length = 255)
    private String contrasena;

    public Credenciales() {
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
