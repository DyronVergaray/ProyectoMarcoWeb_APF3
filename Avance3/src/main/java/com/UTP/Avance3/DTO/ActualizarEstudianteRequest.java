package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos que el estudiante puede modificar desde "Configurar perfil".
 * (La contraseña no se cambia aquí.)
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Datos del perfil que el postulante puede personalizar
 *    (contacto, nivel educativo y descripción).
 */
public record ActualizarEstudianteRequest(

        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres.")
        String nombreEstudiante,

        @NotBlank(message = "El apellido paterno es obligatorio.")
        @Size(max = 100, message = "El apellido paterno no puede superar 100 caracteres.")
        String apellidoPEstudiante,

        @NotBlank(message = "El apellido materno es obligatorio.")
        @Size(max = 100, message = "El apellido materno no puede superar 100 caracteres.")
        String apellidoMEstudiante,

        @NotBlank(message = "El DNI es obligatorio.")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos.")
        String dni,

        @NotBlank(message = "El teléfono es obligatorio.")
        @Pattern(regexp = "\\d{7,15}", message = "El teléfono debe tener entre 7 y 15 dígitos.")
        String telefono,

        @NotBlank(message = "El correo es obligatorio.")
        @Email(regexp = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$", message = "El correo no es válido.")
        @Size(max = 150, message = "El correo no puede superar 150 caracteres.")
        String correo,

        @NotBlank(message = "El nivel educativo es obligatorio.")
        @Size(max = 100, message = "El nivel educativo no puede superar 100 caracteres.")
        String nivelEducativo,

        @NotBlank(message = "La descripción es obligatoria.")
        String descripcion) {
}
