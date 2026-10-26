package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para agregar una experiencia laboral al estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Experiencia laboral que el postulante agrega a su perfil
 *    (entidad, puesto y periodo).
 */
public record ExperienciaLaboralRequest(

        @NotBlank(message = "La entidad es obligatoria.")
        @Size(max = 150, message = "La entidad no puede superar 150 caracteres.")
        String entidad,

        @NotBlank(message = "El puesto es obligatorio.")
        @Size(max = 100, message = "El puesto no puede superar 100 caracteres.")
        String nombrePuesto,

        @NotBlank(message = "El periodo es obligatorio.")
        @Size(max = 100, message = "El periodo no puede superar 100 caracteres.")
        String periodo) {
}
