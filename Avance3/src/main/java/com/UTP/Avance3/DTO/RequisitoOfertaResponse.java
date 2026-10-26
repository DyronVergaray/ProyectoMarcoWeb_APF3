package com.UTP.Avance3.DTO;

/**
 * Un requisito que se puede elegir al crear una oferta.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Requisito que la empresa puede elegir para un anuncio.
 *  - RF-05 (Filtros para búsqueda de trabajo): Requisito que el postulante ve en cada anuncio.
 */
public record RequisitoOfertaResponse(
        Long id,
        String nombreRequisito) {
}
