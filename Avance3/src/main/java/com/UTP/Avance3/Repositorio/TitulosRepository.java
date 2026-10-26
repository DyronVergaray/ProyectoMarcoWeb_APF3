package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.Titulos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a los títulos de los estudiantes.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Lista los títulos de un estudiante y busca uno solo si es suyo,
 *    para poder eliminarlo.
 */
public interface TitulosRepository extends JpaRepository<Titulos, Long> {

    List<Titulos> findByEstudianteIdOrderByIdAsc(Long idEstudiante);

    Optional<Titulos> findByIdAndEstudianteId(Long id, Long idEstudiante);
}
