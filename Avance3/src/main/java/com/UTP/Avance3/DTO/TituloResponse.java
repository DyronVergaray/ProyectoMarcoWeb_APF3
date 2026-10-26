package com.UTP.Avance3.DTO;

/**
 * Un título del estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Título del perfil del postulante.
 *  - RF-04 (Anuncios de trabajo): La empresa lo ve al revisar a un postulante
 *    (PerfilPostulanteResponse).
 */
public record TituloResponse(
        Long id,
        String entidad,
        String nombreTitulo,
        String descargable) {
}
