package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Requisito que puede pedir una oferta (por ejemplo, un nivel de inglés).
 *
 * Requerimientos funcionales:
 *  - RF-04 (Anuncios de trabajo): La empresa lo elige al crear o modificar un anuncio.
 *  - RF-05 (Filtros para búsqueda de trabajo): El postulante lo ve en el detalle del anuncio.
 */
@Entity
@Table(name = "Requisito_Oferta")
public class RequisitoOferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_RequisitoOferta")
    private Long id;

    @Column(name = "Nombre_Requisito", nullable = false, length = 150)
    private String nombreRequisito;

    public RequisitoOferta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreRequisito() {
        return nombreRequisito;
    }

    public void setNombreRequisito(String nombreRequisito) {
        this.nombreRequisito = nombreRequisito;
    }
}
