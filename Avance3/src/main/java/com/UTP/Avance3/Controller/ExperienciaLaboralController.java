package com.UTP.Avance3.Controller;

import com.UTP.Avance3.DTO.ExperienciaLaboralRequest;
import com.UTP.Avance3.DTO.ExperienciaLaboralResponse;
import com.UTP.Avance3.Servicios.ExperienciaLaboralService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de la experiencia laboral del estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): El postulante personaliza su perfil con su experiencia laboral:
 *    GET la lista, POST agrega una y DELETE la elimina. La empresa la ve al revisar a un
 *    postulante.
 */
@RestController
@RequestMapping("/api/estudiantes/{idEstudiante}/experiencias")
public class ExperienciaLaboralController {

    private final ExperienciaLaboralService experienciaService;

    public ExperienciaLaboralController(ExperienciaLaboralService experienciaService) {
        this.experienciaService = experienciaService;
    }

    @GetMapping
    public List<ExperienciaLaboralResponse> listar(@PathVariable Long idEstudiante) {
        return experienciaService.listar(idEstudiante);
    }

    @PostMapping
    public ResponseEntity<ExperienciaLaboralResponse> agregar(@PathVariable Long idEstudiante,
                                                              @Valid @RequestBody ExperienciaLaboralRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(experienciaService.agregar(idEstudiante, request));
    }

    @DeleteMapping("/{idExperiencia}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idEstudiante, @PathVariable Long idExperiencia) {
        experienciaService.eliminar(idEstudiante, idExperiencia);
        return ResponseEntity.noContent().build();
    }
}
