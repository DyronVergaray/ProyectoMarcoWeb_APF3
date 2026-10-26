package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.NotNull;

/**
 * Datos para postular a una oferta. El estudiante viene en la URL;
 * el estado ("Activa") y la fecha (hoy) los asigna el servidor.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Indica la oferta a la que el postulante quiere postularse.
 */
public record PostulacionRequest(

        @NotNull(message = "La oferta es obligatoria.")
        Long idOferta) {
}
