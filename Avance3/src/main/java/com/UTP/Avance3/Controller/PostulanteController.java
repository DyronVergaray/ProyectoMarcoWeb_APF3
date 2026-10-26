package com.UTP.Avance3.Controller;

import com.UTP.Avance3.DTO.CambiarEstadoPostulacionRequest;
import com.UTP.Avance3.DTO.PerfilPostulanteResponse;
import com.UTP.Avance3.DTO.PostulanteResponse;
import com.UTP.Avance3.DTO.PostulantesOfertaResponse;
import com.UTP.Avance3.Servicios.PostulanteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints para que la empresa revise a los postulantes de sus ofertas.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): La empresa ve los postulantes de un anuncio, revisa su
 *    información y los acepta o los descarta (Rechazado).
 *  - RF-03 (Control de acceso basado en roles): Solo el rol Admin, y solo sobre ofertas de su
 *    propia empresa; esa comprobación está en PostulanteService.
 */
@RestController
@RequestMapping("/api/usuarios/{idUsuario}")
public class PostulanteController {

    private final PostulanteService postulanteService;

    public PostulanteController(PostulanteService postulanteService) {
        this.postulanteService = postulanteService;
    }

    // RF-04 (Anuncios de trabajo): Lista de postulantes de un anuncio (postulantes.html).
    // Postulantes de una oferta de la empresa del administrador
    @GetMapping("/ofertas/{idOferta}/postulantes")
    public PostulantesOfertaResponse listar(@PathVariable Long idUsuario, @PathVariable Long idOferta) {
        return postulanteService.listarPostulantes(idUsuario, idOferta);
    }

    // RF-04 (Anuncios de trabajo): Información del postulante para que la empresa decida.
    // RF-06 (Personalizar perfiles): Muestra el perfil que el postulante personalizó.
    // Información del postulante (datos, títulos y experiencia)
    @GetMapping("/postulaciones/{idPostulacion}/postulante")
    public PerfilPostulanteResponse ver(@PathVariable Long idUsuario, @PathVariable Long idPostulacion) {
        return postulanteService.verPostulante(idUsuario, idPostulacion);
    }

    // RF-04 (Anuncios de trabajo): Acepta o descarta al postulante; una vez decidido ya no se puede
    //    cambiar.
    // Aceptar o rechazar una postulación (solo si sigue Activa). @Valid valida el DTO antes de ejecutar
    @PutMapping("/postulaciones/{idPostulacion}/estado")
    public PostulanteResponse cambiarEstado(@PathVariable Long idUsuario,
                                            @PathVariable Long idPostulacion,
                                            @Valid @RequestBody CambiarEstadoPostulacionRequest request) {
        return postulanteService.cambiarEstado(idUsuario, idPostulacion, request);
    }
}
