package com.UTP.Avance3.Controller;

import com.UTP.Avance3.DTO.RequisitoOfertaResponse;
import com.UTP.Avance3.Servicios.RequisitoOfertaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint de los requisitos disponibles para las ofertas.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): La empresa elige de esta lista los requisitos al crear o
 *    modificar un anuncio.
 *  - RF-05 (Filtros para búsqueda de trabajo): Los mismos requisitos se muestran al postulante en
 *    el detalle de cada anuncio.
 */
@RestController
@RequestMapping("/api/requisitos-oferta")
public class RequisitoOfertaController {

    private final RequisitoOfertaService requisitoOfertaService;

    public RequisitoOfertaController(RequisitoOfertaService requisitoOfertaService) {
        this.requisitoOfertaService = requisitoOfertaService;
    }

    @GetMapping
    public List<RequisitoOfertaResponse> listar() {
        return requisitoOfertaService.listar();
    }
}
