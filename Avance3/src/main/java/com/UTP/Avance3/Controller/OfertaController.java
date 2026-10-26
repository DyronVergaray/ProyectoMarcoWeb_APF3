package com.UTP.Avance3.Controller;

import com.UTP.Avance3.DTO.CrearOfertaRequest;
import com.UTP.Avance3.DTO.ModificarOfertaRequest;
import com.UTP.Avance3.DTO.OfertaResponse;
import com.UTP.Avance3.Servicios.OfertaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de las ofertas de trabajo.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): La empresa publica anuncios (POST), los modifica (PUT), incluido
 *    su estado, que son las fases del anuncio, y consulta los suyos (GET /usuarios/{id}/ofertas).
 *  - RF-05 (Filtros para búsqueda de trabajo): GET /ofertas entrega todos los anuncios con los
 *    datos necesarios para que el postulante los filtre y busque.
 *  - RF-03 (Control de acceso basado en roles): Crear, modificar y listar las ofertas de la empresa
 *    es solo para el rol Admin; esa comprobación está en OfertaService.
 */
@RestController
@RequestMapping("/api")
public class OfertaController {

    private final OfertaService ofertaService;

    public OfertaController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    // RF-05 (Filtros para búsqueda de trabajo): Lista pública de anuncios para buscar_trabajo.html.
    // Todas las ofertas (las usa buscar_trabajo.html)
    @GetMapping("/ofertas")
    public List<OfertaResponse> listar() {
        return ofertaService.listar();
    }

    // RF-04 (Anuncios de trabajo): Los anuncios de la empresa del administrador
    //    (mis-vacantes.html).
    // Ofertas de la empresa del administrador (las usa mis-vacantes.html)
    @GetMapping("/usuarios/{idUsuario}/ofertas")
    public List<OfertaResponse> listarDeMiEmpresa(@PathVariable Long idUsuario) {
        return ofertaService.listarPorUsuario(idUsuario);
    }

    // RF-04 (Anuncios de trabajo): Maneja las fases del anuncio: el estado (Activa, En Proceso,
    //    Finalizada) solo cambia por esta vía.
    // El administrador modifica una oferta de su empresa (incluido su estado)
    @PutMapping("/usuarios/{idUsuario}/ofertas/{idOferta}")
    public OfertaResponse modificar(@PathVariable Long idUsuario,
                                    @PathVariable Long idOferta,
                                    @Valid @RequestBody ModificarOfertaRequest request) {
        return ofertaService.modificar(idUsuario, idOferta, request);
    }

    // RF-04 (Anuncios de trabajo): Publica un anuncio nuevo; nace en la fase Activa.
    // Un administrador crea una oferta para su empresa.
    // @Valid hace que Spring valide el DTO antes de ejecutar el método
    @PostMapping("/usuarios/{idUsuario}/ofertas")
    public ResponseEntity<OfertaResponse> crear(@PathVariable Long idUsuario,
                                                @Valid @RequestBody CrearOfertaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ofertaService.crear(idUsuario, request));
    }
}
