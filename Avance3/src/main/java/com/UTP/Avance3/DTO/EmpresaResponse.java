package com.UTP.Avance3.DTO;

/**
 * Datos de la empresa creada que se envían al navegador
 * (nunca incluye la contraseña).
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Resumen de lo que se guardó al registrar la empresa: empresa,
 *    contacto y usuario administrador con su rol.
 */
public record EmpresaResponse(
        Long id,
        String nombreEmpresa,
        String rubro,
        String ruc,
        String telefono,
        String correoEmpresarial,
        String redesSociales,
        Long idUsuarioAdmin,
        String nombreAdmin,
        String correoAdmin,
        String rol) {
}
