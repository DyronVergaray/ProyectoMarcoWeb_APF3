package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.ExperienciaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a la experiencia laboral de los estudiantes.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Lista la experiencia de un estudiante y busca una sola si es
 *    suya, para poder eliminarla.
 */
public interface ExperienciaLaboralRepository extends JpaRepository<ExperienciaLaboral, Long> {

    List<ExperienciaLaboral> findByEstudianteIdOrderByIdAsc(Long idEstudiante);

    Optional<ExperienciaLaboral> findByIdAndEstudianteId(Long id, Long idEstudiante);
}
