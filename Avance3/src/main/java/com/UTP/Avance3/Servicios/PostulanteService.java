package com.UTP.Avance3.Servicios;

import com.UTP.Avance3.DTO.CambiarEstadoPostulacionRequest;
import com.UTP.Avance3.DTO.ExperienciaLaboralResponse;
import com.UTP.Avance3.DTO.PerfilPostulanteResponse;
import com.UTP.Avance3.DTO.PostulanteResponse;
import com.UTP.Avance3.DTO.PostulantesOfertaResponse;
import com.UTP.Avance3.DTO.TituloResponse;
import com.UTP.Avance3.Entidades.EstadoPostulacion;
import com.UTP.Avance3.Entidades.Estudiante;
import com.UTP.Avance3.Entidades.Oferta;
import com.UTP.Avance3.Entidades.Postulacion;
import com.UTP.Avance3.Entidades.Rol;
import com.UTP.Avance3.Entidades.Usuario;
import com.UTP.Avance3.Repositorio.EstadoPostulacionRepository;
import com.UTP.Avance3.Repositorio.ExperienciaLaboralRepository;
import com.UTP.Avance3.Repositorio.OfertaRepository;
import com.UTP.Avance3.Repositorio.PostulacionRepository;
import com.UTP.Avance3.Repositorio.TitulosRepository;
import com.UTP.Avance3.Repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * Lógica de negocio para que el administrador de una empresa revise a los postulantes
 * de sus ofertas. Solo ve y cambia postulaciones de ofertas de su propia empresa.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): La empresa ve a los postulantes de sus anuncios, revisa su
 *    información y los acepta o los descarta. Una vez Aceptado o Rechazado, el estado ya no se
 *    puede cambiar.
 *  - RF-03 (Control de acceso basado en roles): Solo el rol Admin, y solo sobre ofertas de su
 *    propia empresa.
 *  - RF-06 (Personalizar perfiles): Muestra a la empresa el perfil que el postulante personalizó
 *    (datos, títulos y experiencia).
 */
@Service
public class PostulanteService {

    private final UsuarioRepository usuarioRepository;
    private final OfertaRepository ofertaRepository;
    private final PostulacionRepository postulacionRepository;
    private final EstadoPostulacionRepository estadoPostulacionRepository;
    private final TitulosRepository titulosRepository;
    private final ExperienciaLaboralRepository experienciaLaboralRepository;

    public PostulanteService(UsuarioRepository usuarioRepository,
                             OfertaRepository ofertaRepository,
                             PostulacionRepository postulacionRepository,
                             EstadoPostulacionRepository estadoPostulacionRepository,
                             TitulosRepository titulosRepository,
                             ExperienciaLaboralRepository experienciaLaboralRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ofertaRepository = ofertaRepository;
        this.postulacionRepository = postulacionRepository;
        this.estadoPostulacionRepository = estadoPostulacionRepository;
        this.titulosRepository = titulosRepository;
        this.experienciaLaboralRepository = experienciaLaboralRepository;
    }

    /**
     * Lista los postulantes de una oferta de la empresa del administrador.
     *
     * RF-04 (Anuncios de trabajo): Postulantes de un anuncio con su fase.
     */
    @Transactional(readOnly = true)
    public PostulantesOfertaResponse listarPostulantes(Long idUsuario, Long idOferta) {
        Usuario admin = obtenerAdmin(idUsuario);

        Oferta oferta = ofertaRepository.findById(idOferta)
                .orElseThrow(() -> new NoSuchElementException("Oferta no encontrada."));
        verificarEmpresa(admin, oferta);

        return new PostulantesOfertaResponse(
                oferta.getId(),
                oferta.getPuesto(),
                oferta.getEstado(),
                postulacionRepository.findByOfertaIdConEstudiante(idOferta).stream()
                        .map(this::aPostulante)
                        .toList());
    }

    /**
     * Información del postulante: datos personales, títulos y experiencia laboral.
     *
     * RF-04 (Anuncios de trabajo): Información para decidir sobre el postulante. No incluye
     *    contraseña ni DNI.
     * RF-06 (Personalizar perfiles): Datos, títulos y experiencia que el postulante cargó en su
     *    perfil.
     */
    @Transactional(readOnly = true)
    public PerfilPostulanteResponse verPostulante(Long idUsuario, Long idPostulacion) {
        Usuario admin = obtenerAdmin(idUsuario);
        Postulacion postulacion = obtenerPostulacion(idPostulacion);
        verificarEmpresa(admin, postulacion.getOferta());

        Estudiante e = postulacion.getEstudiante();
        return new PerfilPostulanteResponse(
                e.getId(),
                e.getNombreEstudiante(),
                e.getApellidoPEstudiante(),
                e.getApellidoMEstudiante(),
                e.getCorreo(),
                e.getTelefono(),
                e.getNivelEducativo(),
                e.getDescripcion(),
                titulosRepository.findByEstudianteIdOrderByIdAsc(e.getId()).stream()
                        .map(t -> new TituloResponse(t.getId(), t.getEntidad(), t.getNombreTitulo(), t.getDescargable()))
                        .toList(),
                experienciaLaboralRepository.findByEstudianteIdOrderByIdAsc(e.getId()).stream()
                        .map(x -> new ExperienciaLaboralResponse(x.getId(), x.getEntidad(), x.getNombrePuesto(), x.getPeriodo()))
                        .toList());
    }

    /**
     * Pasa una postulación de "Activa" a "Aceptado" o "Rechazado".
     * Una vez aceptada o rechazada, el estado ya no se puede cambiar.
     *
     * RF-04 (Anuncios de trabajo): Descartar (Rechazado) o aceptar (Aceptado) a un postulante. Solo
     *    se permite desde Activa; después la decisión es final.
     */
    @Transactional
    public PostulanteResponse cambiarEstado(Long idUsuario, Long idPostulacion, CambiarEstadoPostulacionRequest request) {
        Usuario admin = obtenerAdmin(idUsuario);
        Postulacion postulacion = obtenerPostulacion(idPostulacion);
        verificarEmpresa(admin, postulacion.getOferta());

        String actual = postulacion.getEstadoPostulacion().getNombreEstado();
        if (!EstadoPostulacion.ACTIVA.equals(actual)) {
            throw new IllegalStateException("Esta postulación ya fue marcada como " + actual
                    + " y su estado no se puede cambiar.");
        }

        EstadoPostulacion nuevo = estadoPostulacionRepository.findByNombreEstado(request.estado())
                .orElseThrow(() -> new IllegalStateException("El estado de postulación no está configurado."));
        postulacion.setEstadoPostulacion(nuevo);
        return aPostulante(postulacionRepository.save(postulacion));
    }

    // RF-03 (Control de acceso basado en roles): Control de acceso por rol: solo el Admin gestiona
    //    postulantes.
    // Usuario que existe y tiene rol Admin; si no, 404 o 403
    private Usuario obtenerAdmin(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado."));
        if (!Rol.ADMIN.equalsIgnoreCase(usuario.getRol().getNombreRol())) {
            throw new SecurityException("Solo un administrador puede gestionar postulantes.");
        }
        return usuario;
    }

    private Postulacion obtenerPostulacion(Long idPostulacion) {
        return postulacionRepository.findByIdCompleta(idPostulacion)
                .orElseThrow(() -> new NoSuchElementException("Postulación no encontrada."));
    }

    // RF-03 (Control de acceso basado en roles): Impide que una empresa vea o decida sobre
    //    postulantes de otra empresa.
    // La oferta debe ser de la misma empresa que el administrador
    private void verificarEmpresa(Usuario admin, Oferta oferta) {
        if (!oferta.getEmpresa().getId().equals(admin.getEmpresa().getId())) {
            throw new SecurityException("Solo puedes gestionar postulantes de ofertas de tu empresa.");
        }
    }

    private PostulanteResponse aPostulante(Postulacion p) {
        Estudiante e = p.getEstudiante();
        return new PostulanteResponse(
                p.getId(),
                e.getId(),
                e.getNombreEstudiante(),
                e.getApellidoPEstudiante(),
                e.getApellidoMEstudiante(),
                e.getCorreo(),
                e.getNivelEducativo(),
                p.getEstadoPostulacion().getNombreEstado(),
                p.getFechaPostulacion());
    }
}
