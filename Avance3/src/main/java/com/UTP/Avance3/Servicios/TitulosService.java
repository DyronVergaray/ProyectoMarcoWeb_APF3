package com.UTP.Avance3.Servicios;

import com.UTP.Avance3.DTO.TituloRequest;
import com.UTP.Avance3.DTO.TituloResponse;
import com.UTP.Avance3.Entidades.Estudiante;
import com.UTP.Avance3.Entidades.Titulos;
import com.UTP.Avance3.Repositorio.EstudianteRepository;
import com.UTP.Avance3.Repositorio.TitulosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Lógica del bloque Títulos del perfil del estudiante.
 *
 * Requerimientos funcionales:
 *  - RF-06 (Personalizar perfiles): Lista, agrega y elimina los títulos del postulante; solo puede
 *    eliminar los suyos.
 */
@Service
public class TitulosService {

    private final TitulosRepository titulosRepository;
    private final EstudianteRepository estudianteRepository;

    public TitulosService(TitulosRepository titulosRepository, EstudianteRepository estudianteRepository) {
        this.titulosRepository = titulosRepository;
        this.estudianteRepository = estudianteRepository;
    }

    /** Lista los títulos de un estudiante. */
    @Transactional(readOnly = true)
    public List<TituloResponse> listar(Long idEstudiante) {
        if (!estudianteRepository.existsById(idEstudiante)) {
            throw new NoSuchElementException("Estudiante no encontrado.");
        }
        return titulosRepository.findByEstudianteIdOrderByIdAsc(idEstudiante).stream()
                .map(this::aRespuesta)
                .toList();
    }

    /** Agrega un título a un estudiante (un estudiante puede tener muchos). */
    @Transactional
    public TituloResponse agregar(Long idEstudiante, TituloRequest request) {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new NoSuchElementException("Estudiante no encontrado."));

        Titulos titulo = new Titulos();
        titulo.setEstudiante(estudiante);
        titulo.setEntidad(request.entidad().trim());
        titulo.setNombreTitulo(request.nombreTitulo().trim());
        // La columna es NOT NULL: si no hay enlace se guarda vacío
        titulo.setDescargable(request.descargable() == null ? "" : request.descargable().trim());

        return aRespuesta(titulosRepository.save(titulo));
    }

    /** Elimina un título, solo si pertenece a ese estudiante. */
    @Transactional
    public void eliminar(Long idEstudiante, Long idTitulo) {
        Titulos titulo = titulosRepository.findByIdAndEstudianteId(idTitulo, idEstudiante)
                .orElseThrow(() -> new NoSuchElementException("Título no encontrado."));
        titulosRepository.delete(titulo);
    }

    private TituloResponse aRespuesta(Titulos t) {
        return new TituloResponse(t.getId(), t.getEntidad(), t.getNombreTitulo(), t.getDescargable());
    }
}
