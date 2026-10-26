package com.UTP.Avance3.DTO;

import java.util.List;

/**
 * Información del postulante que ve la empresa: datos personales, títulos y experiencia.
 * No incluye contraseña ni DNI.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Información que la empresa revisa para decidir sobre un
 *    postulante.
 *  - RF-06 (Personalizar perfiles): Es el perfil que el postulante personalizó: datos, títulos y
 *    experiencia laboral.
 */
public record PerfilPostulanteResponse(
        Long idEstudiante,
        String nombreEstudiante,
        String apellidoPEstudiante,
        String apellidoMEstudiante,
        String correo,
        String telefono,
        String nivelEducativo,
        String descripcion,
        List<TituloResponse> titulos,
        List<ExperienciaLaboralResponse> experiencias) {
}
