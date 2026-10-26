package com.UTP.Avance3.DTO;

/**
 * Datos del estudiante que se envían al navegador (nunca incluye la contraseña).
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Se devuelve al registrarse, para confirmar lo que se guardó.
 *  - RF-02 (Inicio de sesión): Se devuelve al iniciar sesión; el navegador guarda su id para
 *    identificar al postulante.
 *  - RF-06 (Personalizar perfiles): Son los datos que se muestran y se editan en el perfil.
 */
public record EstudianteResponse(
        Long id,
        String nombreEstudiante,
        String apellidoPEstudiante,
        String apellidoMEstudiante,
        String correo,
        String telefono,
        String dni,
        String nivelEducativo,
        String descripcion) {
}
