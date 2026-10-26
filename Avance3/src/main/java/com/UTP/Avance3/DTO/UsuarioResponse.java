package com.UTP.Avance3.DTO;

/**
 * Datos de un usuario que se envían al navegador
 * (sin contraseña ni DNI).
 *
 * Requerimientos funcionales:
 *  - RF-02 (Inicio de sesión): Es la respuesta del inicio de sesión del usuario de empresa.
 *  - RF-03 (Control de acceso basado en roles): Incluye el rol y la empresa del usuario; con el rol
 *    el navegador restringe vistas y opciones.
 *  - RF-06 (Personalizar perfiles): También es cada usuario que se lista en el perfil de la
 *    empresa.
 */
public record UsuarioResponse(
        Long id,
        String nombreUsuario,
        String apellidoPUsuario,
        String apellidoMUsuario,
        String correo,
        String rol,
        Long idEmpresa,
        String nombreEmpresa) {
}
