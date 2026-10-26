package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.NotBlank;

/**
 * Datos del formulario de inicio de sesión.
 *
 * Requerimientos funcionales:
 *  - RF-02 (Inicio de sesión): Correo y contraseña que se comparan para iniciar sesión; lo usan
 *    tanto el postulante como el usuario de empresa.
 */
public record LoginRequest(

        @NotBlank(message = "El correo es obligatorio.")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria.")
        String contrasena) {
}
