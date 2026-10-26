package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Datos del formulario para crear una oferta. La empresa no se envía:
 * se toma de la empresa del usuario administrador que la crea.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Valida el anuncio que publica la empresa: puesto, remuneración,
 *    beneficios, vigencia y requisitos. La fase inicial no se envía: el servidor la fija en Activa.
 */
public record CrearOfertaRequest(

        @NotBlank(message = "El puesto es obligatorio.")
        @Size(max = 100, message = "El puesto no puede superar 100 caracteres.")
        String puesto,

        @NotNull(message = "El sueldo es obligatorio.")
        @DecimalMin(value = "0.01", message = "El sueldo debe ser mayor a 0.")
        @Digits(integer = 8, fraction = 2, message = "El sueldo admite hasta 8 enteros y 2 decimales.")
        BigDecimal sueldo,

        @NotNull(message = "Las horas son obligatorias.")
        @Min(value = 1, message = "Las horas deben estar entre 1 y 168.")
        @Max(value = 168, message = "Las horas deben estar entre 1 y 168.")
        Integer horas,

        @NotBlank(message = "La frecuencia de pagos es obligatoria.")
        @Pattern(regexp = "Semanal|Quincenal|Mensual", message = "La frecuencia de pagos no es válida.")
        String frecuenciaPagos,

        // Opcional: si no se envía se guarda 0
        @DecimalMin(value = "0.00", message = "Los bonos no pueden ser negativos.")
        @Digits(integer = 8, fraction = 2, message = "Los bonos admiten hasta 8 enteros y 2 decimales.")
        BigDecimal bonos,

        // Opcional: si no se envía se guarda vacío (la columna es NOT NULL)
        @Size(max = 100, message = "Las gratificaciones no pueden superar 100 caracteres.")
        String gratificaciones,

        @NotBlank(message = "Indica si incluye seguro de vida.")
        @Pattern(regexp = "Sí|No", message = "El seguro de vida debe ser Sí o No.")
        String seguroVida,

        @NotNull(message = "Los días de vacaciones son obligatorios.")
        @Min(value = 0, message = "Las vacaciones deben estar entre 0 y 365 días.")
        @Max(value = 365, message = "Las vacaciones deben estar entre 0 y 365 días.")
        Integer vacaciones,

        // Opcional: si no se envía se guarda vacío (la columna es NOT NULL)
        @Size(max = 255, message = "El campo otros no puede superar 255 caracteres.")
        String otros,

        @NotNull(message = "La fecha de inicio es obligatoria.")
        @FutureOrPresent(message = "La fecha de inicio no puede ser anterior a hoy.")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria.")
        LocalDate fechaFin,

        // Ids de los requisitos elegidos de la tabla Requisito_Oferta
        List<Long> idsRequisitos) {

    @AssertTrue(message = "La fecha de fin no puede ser anterior a la fecha de inicio.")
    public boolean isFechasValidas() {
        return fechaInicio == null || fechaFin == null || !fechaFin.isBefore(fechaInicio);
    }
}
