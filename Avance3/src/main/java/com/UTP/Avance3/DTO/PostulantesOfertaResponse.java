package com.UTP.Avance3.DTO;

import java.util.List;

/**
 * Los postulantes de una oferta, junto con los datos básicos de la oferta.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Todos los postulantes de un anuncio, junto con el puesto y el
 *    estado del anuncio.
 */
public record PostulantesOfertaResponse(
        Long idOferta,
        String puesto,
        String estadoOferta,
        List<PostulanteResponse> postulantes) {
}
