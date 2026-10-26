package com.UTP.Avance3.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Una postulación con los datos de la oferta a la que se postuló.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Una postulación con su fase (estado) y su fecha; forma el
 *    historial del postulante.
 */
public record PostulacionResponse(
        Long id,
        Long idOferta,
        String puesto,
        Long idEmpresa,
        String nombreEmpresa,
        BigDecimal sueldo,
        String frecuenciaPagos,
        String estado,
        LocalDate fechaPostulacion) {
}
