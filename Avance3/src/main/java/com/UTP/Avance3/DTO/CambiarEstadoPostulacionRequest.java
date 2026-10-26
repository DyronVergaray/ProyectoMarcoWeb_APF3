package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Nuevo estado de una postulación. Solo se puede pasar a Aceptado o Rechazado
 * (y una vez hecho ya no se puede cambiar).
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Decisión de la empresa sobre un postulante: aceptarlo o
 *    descartarlo (Rechazado). Solo admite esos dos valores.
 */
public record CambiarEstadoPostulacionRequest(

        @NotBlank(message = "El estado es obligatorio.")
        @Pattern(regexp = "Aceptado|Rechazado", message = "El estado debe ser Aceptado o Rechazado.")
        String estado) {
}
