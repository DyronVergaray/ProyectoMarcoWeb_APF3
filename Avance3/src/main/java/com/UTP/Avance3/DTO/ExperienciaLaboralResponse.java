package com.UTP.Avance3.DTO;

/**
 * Una experiencia laboral del estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Experiencia del perfil del postulante.
 *  - RF-04 (Anuncios de trabajo): La empresa la ve al revisar a un postulante
 *    (PerfilPostulanteResponse).
 */
public record ExperienciaLaboralResponse(
        Long id,
        String entidad,
        String nombrePuesto,
        String periodo) {
}
