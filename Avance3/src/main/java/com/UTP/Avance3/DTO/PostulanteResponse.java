package com.UTP.Avance3.DTO;

import java.time.LocalDate;

/**
 * Un postulante de una oferta, con el estado de su postulación.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Un postulante de un anuncio con la fase de su postulación.
 */
public record PostulanteResponse(
        Long idPostulacion,
        Long idEstudiante,
        String nombreEstudiante,
        String apellidoPEstudiante,
        String apellidoMEstudiante,
        String correo,
        String nivelEducativo,
        String estado,
        LocalDate fechaPostulacion) {
}
