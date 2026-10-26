package com.UTP.Avance3.Servicios;

import com.UTP.Avance3.DTO.ExperienciaLaboralRequest;
import com.UTP.Avance3.DTO.ExperienciaLaboralResponse;
import com.UTP.Avance3.Entidades.Estudiante;
import com.UTP.Avance3.Entidades.ExperienciaLaboral;
import com.UTP.Avance3.Repositorio.EstudianteRepository;
import com.UTP.Avance3.Repositorio.ExperienciaLaboralRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Lógica del bloque Experiencia laboral del perfil del estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Lista, agrega y elimina la experiencia laboral del postulante;
 *    solo puede eliminar la suya.
 */
@Service
public class ExperienciaLaboralService {

    private final ExperienciaLaboralRepository experienciaRepository;
    private final EstudianteRepository estudianteRepository;

    public ExperienciaLaboralService(ExperienciaLaboralRepository experienciaRepository,
                                     EstudianteRepository estudianteRepository) {
        this.experienciaRepository = experienciaRepository;
        this.estudianteRepository = estudianteRepository;
    }

    /** Lista las experiencias laborales de un estudiante. */
    @Transactional(readOnly = true)
    public List<ExperienciaLaboralResponse> listar(Long idEstudiante) {
        if (!estudianteRepository.existsById(idEstudiante)) {
            throw new NoSuchElementException("Estudiante no encontrado.");
        }
        return experienciaRepository.findByEstudianteIdOrderByIdAsc(idEstudiante).stream()
                .map(this::aRespuesta)
                .toList();
    }

    /** Agrega una experiencia laboral a un estudiante (puede tener muchas). */
    @Transactional
    public ExperienciaLaboralResponse agregar(Long idEstudiante, ExperienciaLaboralRequest request) {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new NoSuchElementException("Estudiante no encontrado."));

        ExperienciaLaboral experiencia = new ExperienciaLaboral();
        experiencia.setEstudiante(estudiante);
        experiencia.setEntidad(request.entidad().trim());
        experiencia.setNombrePuesto(request.nombrePuesto().trim());
        experiencia.setPeriodo(request.periodo().trim());

        return aRespuesta(experienciaRepository.save(experiencia));
    }

    /** Elimina una experiencia, solo si pertenece a ese estudiante. */
    @Transactional
    public void eliminar(Long idEstudiante, Long idExperiencia) {
        ExperienciaLaboral experiencia = experienciaRepository.findByIdAndEstudianteId(idExperiencia, idEstudiante)
                .orElseThrow(() -> new NoSuchElementException("Experiencia laboral no encontrada."));
        experienciaRepository.delete(experiencia);
    }

    private ExperienciaLaboralResponse aRespuesta(ExperienciaLaboral e) {
        return new ExperienciaLaboralResponse(e.getId(), e.getEntidad(), e.getNombrePuesto(), e.getPeriodo());
    }
}
