package com.UTP.Avance3.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos del formulario de configuración de la empresa (configuracion.html).
 * Incluye los datos de la empresa y los de su contacto; no incluye contraseña.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): valida los cambios que el administrador hace al perfil de su
 *    empresa: nombre, rubro y RUC, y los datos de contacto (teléfono, correo empresarial y redes
 *    sociales). Usa las mismas reglas que el registro de la empresa (RegistroEmpresaRequest).
 */
public record ModificarEmpresaRequest(

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
        String redesSociales) {
}
