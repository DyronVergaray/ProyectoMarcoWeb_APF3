package com.UTP.Avance3.Servicios;

import com.UTP.Avance3.DTO.CrearOfertaRequest;
import com.UTP.Avance3.DTO.ModificarOfertaRequest;
import com.UTP.Avance3.DTO.OfertaResponse;
import com.UTP.Avance3.DTO.RequisitoOfertaResponse;
import com.UTP.Avance3.Entidades.ListaRequisitos;
import com.UTP.Avance3.Entidades.Oferta;
import com.UTP.Avance3.Entidades.RequisitoOferta;
import com.UTP.Avance3.Entidades.Rol;
import com.UTP.Avance3.Entidades.Usuario;
import com.UTP.Avance3.Repositorio.ListaRequisitosRepository;
import com.UTP.Avance3.Repositorio.OfertaRepository;
import com.UTP.Avance3.Repositorio.RequisitoOfertaRepository;
import com.UTP.Avance3.Repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Lógica de negocio de las ofertas. El formato de los datos ya fue validado en el DTO,
 * aquí se verifica el permiso (solo Admin) y se guarda la oferta con sus requisitos.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): La empresa crea y modifica anuncios. El estado (Activa, En
 *    Proceso, Finalizada) son las fases del anuncio y solo cambia cuando el administrador lo
 *    modifica.
 *  - RF-05 (Filtros para búsqueda de trabajo): Entrega todos los anuncios, con sus requisitos, para
 *    que el postulante los busque y filtre.
 *  - RF-03 (Control de acceso basado en roles): Solo el rol Admin gestiona ofertas, y solo las de
 *    su empresa.
 */
@Service
public class OfertaService {

    // Toda oferta nueva se crea como activa
    private static final String ESTADO_INICIAL = Oferta.ACTIVA;

    private final OfertaRepository ofertaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RequisitoOfertaRepository requisitoOfertaRepository;
    private final ListaRequisitosRepository listaRequisitosRepository;

    public OfertaService(OfertaRepository ofertaRepository,
                         UsuarioRepository usuarioRepository,
                         RequisitoOfertaRepository requisitoOfertaRepository,
                         ListaRequisitosRepository listaRequisitosRepository) {
        this.ofertaRepository = ofertaRepository;
        this.usuarioRepository = usuarioRepository;
        this.requisitoOfertaRepository = requisitoOfertaRepository;
        this.listaRequisitosRepository = listaRequisitosRepository;
    }

    /**
     * Crea una oferta para la empresa del usuario administrador y la enlaza con los
     * requisitos elegidos (tabla Lista_Requisitos). Si algo falla no se guarda nada.
     *
     * RF-04 (Anuncios de trabajo): Publica un anuncio en la fase Activa y lo enlaza con los
     *    requisitos elegidos (Lista_Requisitos).
     * RF-03 (Control de acceso basado en roles): Solo un Admin puede crearla, y la oferta se asigna
     *    a la empresa de ese usuario.
     */
    @Transactional
    public OfertaResponse crear(Long idUsuario, CrearOfertaRequest request) {
        Usuario usuario = obtenerAdmin(idUsuario);
        List<RequisitoOferta> requisitos = buscarRequisitos(request.idsRequisitos());

        Oferta oferta = new Oferta();
        oferta.setEmpresa(usuario.getEmpresa());
        oferta.setPuesto(request.puesto().trim());
        oferta.setSueldo(request.sueldo());
        oferta.setHoras(request.horas());
        oferta.setFrecuenciaPagos(request.frecuenciaPagos());
        // Las columnas son NOT NULL: sin dato se guarda 0 o vacío
        oferta.setBonos(request.bonos() == null ? BigDecimal.ZERO : request.bonos());
        oferta.setGratificaciones(request.gratificaciones() == null ? "" : request.gratificaciones().trim());
        oferta.setSeguroVida(request.seguroVida());
        oferta.setVacaciones(request.vacaciones());
        oferta.setOtros(request.otros() == null ? "" : request.otros().trim());
        oferta.setEstado(ESTADO_INICIAL);
        oferta.setFechaInicio(request.fechaInicio());
        oferta.setFechaFin(request.fechaFin());
        oferta = ofertaRepository.save(oferta);

        List<ListaRequisitos> lista = new ArrayList<>();
        for (RequisitoOferta requisito : requisitos) {
            ListaRequisitos fila = new ListaRequisitos();
            fila.setOferta(oferta);
            fila.setRequisitoOferta(requisito);
            lista.add(fila);
        }
        listaRequisitosRepository.saveAll(lista);

        return aRespuesta(oferta, requisitos.stream().map(this::aRequisito).toList());
    }

    /**
     * Lista todas las ofertas (las más recientes primero) con sus requisitos.
     *
     * RF-05 (Filtros para búsqueda de trabajo): Todos los anuncios con sus requisitos. El filtrado
     *    (texto del puesto, empresa, estado, frecuencia, sueldo, horas, seguro y bonos) lo hace
     *    buscar_trabajo.js en el navegador. Pendiente: filtrar por área.
     */
    @Transactional(readOnly = true)
    public List<OfertaResponse> listar() {
        return aRespuestas(ofertaRepository.findAllConEmpresa());
    }

    /**
     * Lista las ofertas de la empresa del administrador (para mis-vacantes.html).
     *
     * RF-04 (Anuncios de trabajo): Anuncios de la empresa del administrador.
     * RF-03 (Control de acceso basado en roles): Un usuario que no es Admin no puede verlos.
     */
    @Transactional(readOnly = true)
    public List<OfertaResponse> listarPorUsuario(Long idUsuario) {
        Usuario usuario = obtenerAdmin(idUsuario);
        return aRespuestas(ofertaRepository.findByEmpresaId(usuario.getEmpresa().getId()));
    }

    /**
     * Modifica una oferta de la empresa del administrador, incluido su estado.
     * Este es el único lugar donde cambia el estado de una oferta: nada lo cambia solo.
     *
     * RF-04 (Anuncios de trabajo): Maneja las fases del anuncio: este es el único lugar donde
     *    cambia el estado de una oferta y nada lo cambia automáticamente (ni siquiera al vencer la
     *    fecha).
     * RF-03 (Control de acceso basado en roles): Solo el Admin de la misma empresa puede
     *    modificarla.
     */
    @Transactional
    public OfertaResponse modificar(Long idUsuario, Long idOferta, ModificarOfertaRequest request) {
        Usuario usuario = obtenerAdmin(idUsuario);

        Oferta oferta = ofertaRepository.findById(idOferta)
                .orElseThrow(() -> new NoSuchElementException("Oferta no encontrada."));
        if (!oferta.getEmpresa().getId().equals(usuario.getEmpresa().getId())) {
            throw new SecurityException("Solo puedes modificar las ofertas de tu empresa.");
        }

        List<RequisitoOferta> requisitos = buscarRequisitos(request.idsRequisitos());

        oferta.setPuesto(request.puesto().trim());
        oferta.setSueldo(request.sueldo());
        oferta.setHoras(request.horas());
        oferta.setFrecuenciaPagos(request.frecuenciaPagos());
        oferta.setBonos(request.bonos() == null ? BigDecimal.ZERO : request.bonos());
        oferta.setGratificaciones(request.gratificaciones() == null ? "" : request.gratificaciones().trim());
        oferta.setSeguroVida(request.seguroVida());
        oferta.setVacaciones(request.vacaciones());
        oferta.setOtros(request.otros() == null ? "" : request.otros().trim());
        oferta.setEstado(request.estado());
        oferta.setFechaInicio(request.fechaInicio());
        oferta.setFechaFin(request.fechaFin());
        oferta = ofertaRepository.save(oferta);

        // Requisitos: se quitan los que ya no están elegidos y se agregan los nuevos
        List<ListaRequisitos> actuales = listaRequisitosRepository.findByOfertaIds(List.of(oferta.getId()));
        Set<Long> nuevosIds = new HashSet<>();
        requisitos.forEach(r -> nuevosIds.add(r.getId()));
        Set<Long> actualesIds = new HashSet<>();

        List<ListaRequisitos> quitar = new ArrayList<>();
        for (ListaRequisitos fila : actuales) {
            Long idRequisito = fila.getRequisitoOferta().getId();
            if (nuevosIds.contains(idRequisito)) {
                actualesIds.add(idRequisito);
            } else {
                quitar.add(fila);
            }
        }
        listaRequisitosRepository.deleteAll(quitar);

        List<ListaRequisitos> agregar = new ArrayList<>();
        for (RequisitoOferta requisito : requisitos) {
            if (!actualesIds.contains(requisito.getId())) {
                ListaRequisitos fila = new ListaRequisitos();
                fila.setOferta(oferta);
                fila.setRequisitoOferta(requisito);
                agregar.add(fila);
            }
        }
        listaRequisitosRepository.saveAll(agregar);

        return aRespuesta(oferta, requisitos.stream().map(this::aRequisito).toList());
    }

    // RF-03 (Control de acceso basado en roles): Control de acceso por rol: si el usuario no tiene
    //    el rol Admin se responde 403. Por ahora el id del usuario llega en la URL; con JWT o
    //    Spring Security saldría del token.
    // Usuario que existe y tiene rol Admin; si no, 404 o 403
    private Usuario obtenerAdmin(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado."));
        if (!Rol.ADMIN.equalsIgnoreCase(usuario.getRol().getNombreRol())) {
            throw new SecurityException("Solo un administrador puede gestionar ofertas.");
        }
        return usuario;
    }

    // Requisitos elegidos (sin repetidos); todos deben existir en Requisito_Oferta
    private List<RequisitoOferta> buscarRequisitos(List<Long> idsRecibidos) {
        List<Long> ids = idsRecibidos == null
                ? List.of()
                : idsRecibidos.stream().filter(Objects::nonNull).distinct().toList();
        List<RequisitoOferta> requisitos = requisitoOfertaRepository.findAllById(ids);
        if (requisitos.size() != ids.size()) {
            throw new IllegalArgumentException("Alguno de los requisitos seleccionados no existe.");
        }
        return requisitos;
    }

    // Convierte ofertas en DTO trayendo los requisitos de todas en una sola consulta
    private List<OfertaResponse> aRespuestas(List<Oferta> ofertas) {
        if (ofertas.isEmpty()) {
            return List.of();
        }

        // Requisitos de todas las ofertas en una sola consulta, agrupados por oferta
        List<Long> ids = ofertas.stream().map(Oferta::getId).toList();
        Map<Long, List<RequisitoOfertaResponse>> requisitosPorOferta =
                listaRequisitosRepository.findByOfertaIds(ids).stream()
                        .collect(Collectors.groupingBy(
                                fila -> fila.getOferta().getId(),
                                Collectors.mapping(fila -> aRequisito(fila.getRequisitoOferta()),
                                        Collectors.toList())));

        return ofertas.stream()
                .map(o -> aRespuesta(o, requisitosPorOferta.getOrDefault(o.getId(), List.of())))
                .toList();
    }

    private RequisitoOfertaResponse aRequisito(RequisitoOferta r) {
        return new RequisitoOfertaResponse(r.getId(), r.getNombreRequisito());
    }

    private OfertaResponse aRespuesta(Oferta o, List<RequisitoOfertaResponse> requisitos) {
        return new OfertaResponse(
                o.getId(),
                o.getEmpresa().getId(),
                o.getEmpresa().getNombreEmpresa(),
                o.getPuesto(),
                o.getSueldo(),
                o.getHoras(),
                o.getFrecuenciaPagos(),
                o.getBonos(),
                o.getGratificaciones(),
                o.getSeguroVida(),
                o.getVacaciones(),
                o.getOtros(),
                o.getEstado(),
                o.getFechaInicio(),
                o.getFechaFin(),
                requisitos);
    }
}
