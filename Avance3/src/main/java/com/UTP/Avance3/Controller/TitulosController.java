package com.UTP.Avance3.Controller;

import com.UTP.Avance3.DTO.TituloRequest;
import com.UTP.Avance3.DTO.TituloResponse;
import com.UTP.Avance3.Servicios.TitulosService;
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
 * Endpoints de los títulos del estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): El postulante personaliza su perfil con sus títulos: GET los
 *    lista, POST agrega uno y DELETE lo elimina. La empresa los ve al revisar a un postulante.
 */
@RestController
@RequestMapping("/api/estudiantes/{idEstudiante}/titulos")
public class TitulosController {

    private final TitulosService titulosService;

    public TitulosController(TitulosService titulosService) {
        this.titulosService = titulosService;
    }

    @GetMapping
    public List<TituloResponse> listar(@PathVariable Long idEstudiante) {
        return titulosService.listar(idEstudiante);
    }

    @PostMapping
    public ResponseEntity<TituloResponse> agregar(@PathVariable Long idEstudiante,
                                                  @Valid @RequestBody TituloRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(titulosService.agregar(idEstudiante, request));
    }

    @DeleteMapping("/{idTitulo}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idEstudiante, @PathVariable Long idTitulo) {
        titulosService.eliminar(idEstudiante, idTitulo);
        return ResponseEntity.noContent().build();
    }
}
