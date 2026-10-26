package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.ListaRequisitos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Acceso a los requisitos elegidos para cada oferta.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Guarda y actualiza los requisitos de un anuncio.
 *  - RF-05 (Filtros para búsqueda de trabajo): findByOfertaIds trae los requisitos de muchos
 *    anuncios en una sola consulta para mostrarlos al postulante.
 */
public interface ListaRequisitosRepository extends JpaRepository<ListaRequisitos, Long> {

    // Requisitos de varias ofertas en una sola consulta
    @Query("select l from ListaRequisitos l join fetch l.oferta join fetch l.requisitoOferta "
            + "where l.oferta.id in :idsOfertas order by l.id")
    List<ListaRequisitos> findByOfertaIds(@Param("idsOfertas") List<Long> idsOfertas);
}
