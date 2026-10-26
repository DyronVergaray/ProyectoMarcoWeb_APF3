package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.RequisitoOferta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a los requisitos de las ofertas.
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Entrega los requisitos que la empresa puede elegir;
 *    DatosIniciales los crea al arrancar usando existsByNombreRequisito.
 *  - RF-05 (Filtros para búsqueda de trabajo): Son los requisitos que el postulante ve en los
 *    anuncios.
 */
public interface RequisitoOfertaRepository extends JpaRepository<RequisitoOferta, Long> {

    boolean existsByNombreRequisito(String nombreRequisito);

    List<RequisitoOferta> findAllByOrderByIdAsc();
}
