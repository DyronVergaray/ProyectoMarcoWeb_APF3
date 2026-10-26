package com.UTP.Avance3.Config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Convierte los errores en respuestas JSON con el campo "mensaje".
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Si los datos del registro no pasan la validación del DTO
 *    responde 400, y si el correo, el DNI o el RUC ya existen responde 409, para que no se
 *    registren cuentas repetidas.
 *  - RF-02 (Inicio de sesión): Con credenciales incorrectas responde 400 con un mensaje, y así se
 *    rechaza el acceso sin revelar si falló el correo o la contraseña.
 *  - RF-03 (Control de acceso basado en roles): Cuando un usuario intenta una acción que no
 *    corresponde a su rol o a su empresa responde 403.
 *  - RF-04 (Anuncios de trabajo): El 409 también avisa cuando se intenta decidir una postulación
 *    que ya fue Aceptada o Rechazada, o postular a una oferta que no está Activa.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    // RF-01 (Registro de usuarios): Devuelve el detalle de cada campo inválido del formulario de
    //    registro (y de los demás formularios).
    // Falló alguna validación del DTO (@NotBlank, @Pattern, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException e) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", "Revisa los datos ingresados.", "errores", errores));
    }

    // El JSON enviado está mal formado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", "El formato de los datos enviados no es válido."));
    }

    // RF-02 (Inicio de sesión): Aquí termina el inicio de sesión con correo o contraseña
    //    incorrectos: el acceso se rechaza.
    // Credenciales incorrectas
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> datosInvalidos(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
    }

    // RF-01 (Registro de usuarios): Correo, DNI o RUC ya registrados.
    // RF-04 (Anuncios de trabajo): Postulación ya decidida, oferta que no recibe postulaciones o
    //    postulación repetida.
    // Correo o DNI ya registrado
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> duplicado(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
    }

    // RF-03 (Control de acceso basado en roles): Respuesta 403 cuando el rol o la empresa del
    //    usuario no permiten la acción.
    // El usuario no tiene el rol necesario para la acción
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> sinPermiso(SecurityException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", e.getMessage()));
    }

    // No se encontró el registro
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
    }
}
