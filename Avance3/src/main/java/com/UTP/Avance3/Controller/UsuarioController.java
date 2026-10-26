package com.UTP.Avance3.Controller;

import com.UTP.Avance3.DTO.LoginRequest;
import com.UTP.Avance3.DTO.UsuarioResponse;
import com.UTP.Avance3.Servicios.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de los usuarios de una empresa.
 *
 * Requerimientos funcionales:
 *  - RF-02 (Inicio de sesión): POST /login inicia sesión al usuario de empresa con su correo y
 *    contraseña.
 *  - RF-03 (Control de acceso basado en roles): La respuesta incluye el rol del usuario; con ese
 *    dato el navegador decide qué vistas y opciones mostrarle.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // RF-02 (Inicio de sesión): Inicio de sesión del usuario de empresa.
    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest request) {
        return usuarioService.iniciarSesion(request);
    }
}
