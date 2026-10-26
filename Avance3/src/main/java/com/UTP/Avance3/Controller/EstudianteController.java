package com.UTP.Avance3.Controller;

import com.UTP.Avance3.DTO.ActualizarEstudianteRequest;
import com.UTP.Avance3.DTO.EstudianteResponse;
import com.UTP.Avance3.DTO.LoginRequest;
import com.UTP.Avance3.DTO.RegistroEstudianteRequest;
import com.UTP.Avance3.Servicios.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints del estudiante (postulante): registro, inicio de sesión y datos de su perfil.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): POST /registro crea la cuenta del postulante con sus datos
 *    básicos.
 *  - RF-02 (Inicio de sesión): POST /login valida el correo y la contraseña del postulante.
 *  - RF-06 (Personalizar perfiles): GET y PUT /{id} muestran y actualizan los datos personales y de
 *    contacto del perfil del postulante.
 */
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    // RF-01 (Registro de usuarios): Registra al postulante. Los datos se validan en
    //    RegistroEstudianteRequest antes de entrar al servicio.
    // @Valid hace que Spring valide el DTO antes de ejecutar el método
    @PostMapping("/registro")
    public ResponseEntity<EstudianteResponse> registrar(@Valid @RequestBody RegistroEstudianteRequest request) {
        EstudianteResponse respuesta = estudianteService.registrarEstudiante(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // RF-02 (Inicio de sesión): Inicio de sesión del postulante.
    @PostMapping("/login")
    public EstudianteResponse login(@Valid @RequestBody LoginRequest request) {
        return estudianteService.iniciarSesion(request);
    }

    // RF-06 (Personalizar perfiles): Datos del perfil que se muestran en Configurar perfil.
    @GetMapping("/{id}")
    public EstudianteResponse obtener(@PathVariable Long id) {
        return estudianteService.obtenerPorId(id);
    }

    // RF-06 (Personalizar perfiles): El postulante personaliza su perfil (datos personales,
    //    contacto, nivel educativo y descripción).
    @PutMapping("/{id}")
    public EstudianteResponse actualizar(@PathVariable Long id,
                                         @Valid @RequestBody ActualizarEstudianteRequest request) {
        return estudianteService.actualizarEstudiante(id, request);
    }
}
