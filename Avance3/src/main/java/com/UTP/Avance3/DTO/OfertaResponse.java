package com.UTP.Avance3.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Datos de la oferta creada que se envían al navegador.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Es el anuncio creado o modificado que ve la empresa.
 *  - RF-05 (Filtros para búsqueda de trabajo): Es el anuncio que ve el postulante, con los datos
 *    (empresa, sueldo, frecuencia, horas, bonos, seguro, estado y requisitos) sobre los que se
 *    filtra.
 */
public record OfertaResponse(
        Long id,
        Long idEmpresa,
        String nombreEmpresa,
        String puesto,
        BigDecimal sueldo,
        Integer horas,
        String frecuenciaPagos,
        BigDecimal bonos,
        String gratificaciones,
        String seguroVida,
        Integer vacaciones,
        String otros,
        String estado,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<RequisitoOfertaResponse> requisitos) {
}
