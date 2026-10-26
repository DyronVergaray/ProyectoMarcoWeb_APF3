package com.UTP.Avance3.DTO;

import java.util.List;

/**
 * Datos de la empresa para la página perfil.html, junto con
 * todos los usuarios que pertenecen a ella (cada uno con su rol).
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Perfil de la empresa: sus datos, su contacto y los usuarios que
 *    pertenecen a ella con su rol.
 */
public record PerfilEmpresaResponse(
        Long id,
        String nombreEmpresa,
        String rubro,
        String ruc,
        String telefono,
        String correoEmpresarial,
        String redesSociales,
        List<UsuarioResponse> usuarios) {
}
