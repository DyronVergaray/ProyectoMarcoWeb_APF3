package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos del formulario de registro de empresa. Incluye tres bloques:
 * la empresa, su contacto y el usuario administrador que se crea con ella.
 * Aquí se validan los campos: si algo no cumple, Spring responde 400
 * antes de llegar al servicio.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Valida en un solo formulario los datos de la empresa, los de su
 *    contacto y los del usuario administrador que se crea con ella.
 *  - RF-06 (Personalizar perfiles): Incluye el contacto de la empresa (teléfono, correo empresarial
 *    y redes sociales), que luego se muestra en su perfil.
 */
public record RegistroEmpresaRequest(

        // ---------- Datos de la empresa ----------
        @NotBlank(message = "El nombre de la empresa es obligatorio.")
        @Size(max = 150, message = "El nombre de la empresa no puede superar 150 caracteres.")
        String nombreEmpresa,

        @NotBlank(message = "El rubro es obligatorio.")
        @Size(max = 100, message = "El rubro no puede superar 100 caracteres.")
        String rubro,

        @NotBlank(message = "El RUC es obligatorio.")
        @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 dígitos.")
        String ruc,

        // ---------- Contacto de la empresa ----------
        @NotBlank(message = "El teléfono de la empresa es obligatorio.")
        @Pattern(regexp = "\\d{7,15}", message = "El teléfono debe tener entre 7 y 15 dígitos.")
        String telefono,

        @NotBlank(message = "El correo empresarial es obligatorio.")
        @Email(regexp = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$", message = "El correo empresarial no es válido.")
        @Size(max = 150, message = "El correo empresarial no puede superar 150 caracteres.")
        String correoEmpresarial,

        // Opcional: si no se envía se guarda vacío (la columna es NOT NULL)
        @Size(max = 255, message = "Las redes sociales no pueden superar 255 caracteres.")
        String redesSociales,

        // ---------- Usuario administrador ----------
        @NotBlank(message = "El nombre del administrador es obligatorio.")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres.")
        String nombreUsuario,

        @NotBlank(message = "El apellido paterno es obligatorio.")
        @Size(max = 100, message = "El apellido paterno no puede superar 100 caracteres.")
        String apellidoPUsuario,

        @NotBlank(message = "El apellido materno es obligatorio.")
        @Size(max = 100, message = "El apellido materno no puede superar 100 caracteres.")
        String apellidoMUsuario,

        @NotBlank(message = "El DNI es obligatorio.")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos.")
        String dni,

        @NotBlank(message = "El correo del administrador es obligatorio.")
        @Email(regexp = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$", message = "El correo del administrador no es válido.")
        @Size(max = 150, message = "El correo del administrador no puede superar 150 caracteres.")
        String correo,

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
