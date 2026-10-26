package com.UTP.Avance3.Servicios;

import com.UTP.Avance3.DTO.PostulacionRequest;
import com.UTP.Avance3.DTO.PostulacionResponse;
import com.UTP.Avance3.Entidades.Estudiante;
import com.UTP.Avance3.Entidades.EstadoPostulacion;
import com.UTP.Avance3.Entidades.Oferta;
import com.UTP.Avance3.Entidades.Postulacion;
import com.UTP.Avance3.Repositorio.EstadoPostulacionRepository;
import com.UTP.Avance3.Repositorio.EstudianteRepository;
import com.UTP.Avance3.Repositorio.OfertaRepository;
import com.UTP.Avance3.Repositorio.PostulacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Lógica de negocio de las postulaciones. El formato ya fue validado en el DTO,
 * aquí se aplican las reglas (no repetir postulación, oferta vigente) y se guarda.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): El postulante se postula a anuncios y consulta su historial. Solo
 *    se puede postular a ofertas en fase Activa y una sola vez por oferta; toda postulación nueva
 *    empieza en Activa.
 */
@Service
public class PostulacionService {

    private final PostulacionRepository postulacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final OfertaRepository ofertaRepository;
    private final EstadoPostulacionRepository estadoPostulacionRepository;

    public PostulacionService(PostulacionRepository postulacionRepository,
                              EstudianteRepository estudianteRepository,
                              OfertaRepository ofertaRepository,
                              EstadoPostulacionRepository estadoPostulacionRepository) {
        this.postulacionRepository = postulacionRepository;
        this.estudianteRepository = estudianteRepository;
        this.ofertaRepository = ofertaRepository;
        this.estadoPostulacionRepository = estadoPostulacionRepository;
    }

    /**
     * El estudiante postula a una oferta: estado "Activa" y fecha de hoy.
     *
     * RF-04 (Anuncios de trabajo): Inicia el proceso de selección: crea la postulación en estado
     *    Activa con la fecha de hoy. Se rechaza si la oferta está En Proceso o Finalizada.
     */
    @Transactional
    public PostulacionResponse postular(Long idEstudiante, PostulacionRequest request) {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new NoSuchElementException("Estudiante no encontrado."));
        Oferta oferta = ofertaRepository.findById(request.idOferta())
                .orElseThrow(() -> new NoSuchElementException("Oferta no encontrada."));

        // Solo se puede postular cuando la oferta está "Activa" (el estado lo cambia el administrador)
        if (!Oferta.ACTIVA.equals(oferta.getEstado())) {
            throw new IllegalStateException("Esta oferta no está recibiendo postulaciones (estado: " + oferta.getEstado() + ").");
        }
        if (postulacionRepository.existsByEstudianteIdAndOfertaId(idEstudiante, oferta.getId())) {
            throw new IllegalStateException("Ya postulaste a esta oferta.");
        }

        EstadoPostulacion activa = estadoPostulacionRepository.findByNombreEstado(EstadoPostulacion.ACTIVA)
                .orElseThrow(() -> new IllegalStateException("El estado inicial de postulación no está configurado."));

        Postulacion postulacion = new Postulacion();
        postulacion.setEstudiante(estudiante);
        postulacion.setOferta(oferta);
        postulacion.setEstadoPostulacion(activa);
        postulacion.setFechaPostulacion(LocalDate.now());
        postulacion = postulacionRepository.save(postulacion);

        return aRespuesta(postulacion);
    }

    /**
     * Historial de postulaciones del estudiante, la más reciente primero.
     *
     * RF-04 (Anuncios de trabajo): Historial con la fase (estado) de cada postulación.
     */
    @Transactional(readOnly = true)
    public List<PostulacionResponse> listarPorEstudiante(Long idEstudiante) {
        if (!estudianteRepository.existsById(idEstudiante)) {
            throw new NoSuchElementException("Estudiante no encontrado.");
        }
        return postulacionRepository.findHistorialByEstudiante(idEstudiante).stream()
                .map(this::aRespuesta)
                .toList();
    }

    private PostulacionResponse aRespuesta(Postulacion p) {
        Oferta o = p.getOferta();
        return new PostulacionResponse(
                p.getId(),
                o.getId(),
                o.getPuesto(),
                o.getEmpresa().getId(),
                o.getEmpresa().getNombreEmpresa(),
                o.getSueldo(),
                o.getFrecuenciaPagos(),
                p.getEstadoPostulacion().getNombreEstado(),
                p.getFechaPostulacion());
    }
}
