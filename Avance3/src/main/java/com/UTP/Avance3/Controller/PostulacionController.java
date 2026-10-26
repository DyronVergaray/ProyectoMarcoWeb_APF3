package com.UTP.Avance3.Controller;

import com.UTP.Avance3.DTO.PostulacionRequest;
import com.UTP.Avance3.DTO.PostulacionResponse;
import com.UTP.Avance3.Servicios.PostulacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de las postulaciones del estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): POST crea la postulación en su fase inicial (Activa) y GET
 *    devuelve el historial, donde el postulante ve en qué fase está cada una.
 */
@RestController
@RequestMapping("/api/estudiantes/{idEstudiante}/postulaciones")
public class PostulacionController {

    private final PostulacionService postulacionService;

    public PostulacionController(PostulacionService postulacionService) {
        this.postulacionService = postulacionService;
    }

    // RF-04 (Anuncios de trabajo): Historial de postulaciones con su estado (postulaciones.html).
    // Historial de postulaciones del estudiante
    @GetMapping
    public List<PostulacionResponse> listar(@PathVariable Long idEstudiante) {
        return postulacionService.listarPorEstudiante(idEstudiante);
    }

    // RF-04 (Anuncios de trabajo): El postulante se postula a un anuncio (botón Postular).
    // Postular a una oferta. @Valid hace que Spring valide el DTO antes de ejecutar el método
    @PostMapping
    public ResponseEntity<PostulacionResponse> postular(@PathVariable Long idEstudiante,
                                                        @Valid @RequestBody PostulacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postulacionService.postular(idEstudiante, request));
    }
}
