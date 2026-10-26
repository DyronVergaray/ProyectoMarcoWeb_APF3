package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a las postulaciones.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Evita postulaciones repetidas, entrega el historial del
 *    postulante y los postulantes de un anuncio, y trae una postulación completa para verificar a
 *    qué empresa pertenece antes de decidir.
 */
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    boolean existsByEstudianteIdAndOfertaId(Long idEstudiante, Long idOferta);

    // Historial del estudiante con la oferta, la empresa y el estado en una sola consulta (la más reciente primero)
    @Query("select p from Postulacion p "
            + "join fetch p.oferta o join fetch o.empresa join fetch p.estadoPostulacion "
            + "where p.estudiante.id = :idEstudiante "
            + "order by p.fechaPostulacion desc, p.id desc")
    List<Postulacion> findHistorialByEstudiante(@Param("idEstudiante") Long idEstudiante);

    // Postulantes de una oferta, con su estudiante y estado (la más reciente primero)
    @Query("select p from Postulacion p "
            + "join fetch p.estudiante join fetch p.estadoPostulacion "
            + "where p.oferta.id = :idOferta "
            + "order by p.fechaPostulacion desc, p.id desc")
    List<Postulacion> findByOfertaIdConEstudiante(@Param("idOferta") Long idOferta);

    // Una postulación con todo lo necesario para verificar a qué empresa pertenece
    @Query("select p from Postulacion p "
            + "join fetch p.estudiante join fetch p.estadoPostulacion "
            + "join fetch p.oferta o join fetch o.empresa "
            + "where p.id = :id")
    Optional<Postulacion> findByIdCompleta(@Param("id") Long id);
}
