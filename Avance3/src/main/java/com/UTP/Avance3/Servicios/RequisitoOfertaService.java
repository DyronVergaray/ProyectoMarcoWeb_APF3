package com.UTP.Avance3.Servicios;

import com.UTP.Avance3.DTO.RequisitoOfertaResponse;
import com.UTP.Avance3.Repositorio.RequisitoOfertaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de los requisitos disponibles para las ofertas.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Entrega la lista de requisitos que la empresa puede elegir para
 *    un anuncio.
 *  - RF-05 (Filtros para búsqueda de trabajo): Es la misma lista de requisitos que el postulante ve
 *    en los anuncios.
 */
@Service
public class RequisitoOfertaService {

    private final RequisitoOfertaRepository requisitoOfertaRepository;

    public RequisitoOfertaService(RequisitoOfertaRepository requisitoOfertaRepository) {
        this.requisitoOfertaRepository = requisitoOfertaRepository;
    }

    /** Lista todos los requisitos disponibles para elegir en una oferta. */
    @Transactional(readOnly = true)
    public List<RequisitoOfertaResponse> listar() {
        return requisitoOfertaRepository.findAllByOrderByIdAsc().stream()
                .map(r -> new RequisitoOfertaResponse(r.getId(), r.getNombreRequisito()))
                .toList();
    }
}
