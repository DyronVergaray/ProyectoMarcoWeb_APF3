package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.EstadoPostulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a las fases de una postulación.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Busca por nombre la fase Activa (al postular) o Aceptado y
 *    Rechazado (al decidir).
 */
public interface EstadoPostulacionRepository extends JpaRepository<EstadoPostulacion, Long> {

    boolean existsByNombreEstado(String nombreEstado);

    Optional<EstadoPostulacion> findByNombreEstado(String nombreEstado);
}
