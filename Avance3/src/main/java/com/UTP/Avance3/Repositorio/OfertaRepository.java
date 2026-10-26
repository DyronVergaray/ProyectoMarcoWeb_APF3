package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Acceso a las ofertas de trabajo.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): findByEmpresaId entrega los anuncios de una empresa
 *    (mis-vacantes.html).
 *  - RF-05 (Filtros para búsqueda de trabajo): findAllConEmpresa entrega todos los anuncios con su
 *    empresa en una sola consulta.
 */
public interface OfertaRepository extends JpaRepository<Oferta, Long> {

    // Trae las ofertas con su empresa en una sola consulta, las más recientes primero
    @Query("select o from Oferta o join fetch o.empresa order by o.id desc")
    List<Oferta> findAllConEmpresa();

    // Ofertas de una sola empresa, las más recientes primero
    @Query("select o from Oferta o join fetch o.empresa where o.empresa.id = :idEmpresa order by o.id desc")
    List<Oferta> findByEmpresaId(@Param("idEmpresa") Long idEmpresa);
}
