package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Une una oferta con cada uno de sus requisitos (una oferta tiene muchos requisitos y un requisito está en muchas ofertas).
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): Guarda los requisitos elegidos para cada anuncio.
 *  - RF-05 (Filtros para búsqueda de trabajo): Permite mostrar los requisitos de cada anuncio al
 *    postulante.
 */
@Entity
@Table(name = "Lista_Requisitos")
public class ListaRequisitos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_ListaRequisitos")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Oferta", nullable = false)
    private Oferta oferta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_RequisitoOferta", nullable = false)
    private RequisitoOferta requisitoOferta;

    public ListaRequisitos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Oferta getOferta() {
        return oferta;
    }

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    public RequisitoOferta getRequisitoOferta() {
        return requisitoOferta;
    }

    public void setRequisitoOferta(RequisitoOferta requisitoOferta) {
        this.requisitoOferta = requisitoOferta;
    }
}
