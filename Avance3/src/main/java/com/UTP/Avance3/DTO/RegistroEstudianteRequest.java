package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos del formulario de registro. Aquí se validan los campos:
 * si algo no cumple, Spring responde 400 antes de llegar al servicio.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Valida los datos básicos del postulante (nombres, correo, DNI,
 *    teléfono, nivel educativo y contraseña) antes de registrarlo.
 */
public record RegistroEstudianteRequest(

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
        String descripcion,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 6, max = 255, message = "La contraseña debe tener entre 6 y 255 caracteres.")
        String contrasena,

        @NotBlank(message = "Debes confirmar la contraseña.")
        String confirmarContrasena) {

    @AssertTrue(message = "Las contraseñas no coinciden.")
    public boolean isContrasenasIguales() {
        return contrasena == null || contrasena.equals(confirmarContrasena);
    }
}
