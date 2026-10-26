package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos para agregar un título al estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Título que el postulante agrega a su perfil (entidad, nombre y
 *    enlace opcional al certificado).
 */
public record TituloRequest(

        @NotBlank(message = "La entidad es obligatoria.")
        @Size(max = 150, message = "La entidad no puede superar 150 caracteres.")
        String entidad,

        @NotBlank(message = "El nombre del título es obligatorio.")
        @Size(max = 150, message = "El nombre del título no puede superar 150 caracteres.")
        String nombreTitulo,

        // Opcional: enlace para descargar el certificado (solo http o https)
        @Size(max = 255, message = "El enlace no puede superar 255 caracteres.")
        @Pattern(regexp = "(https?://\\S+)?", message = "El enlace debe empezar con http:// o https://")
        String descargable) {
}
