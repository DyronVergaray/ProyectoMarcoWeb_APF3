package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "Estado_Calificacion")
public class EstadoCalificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_EstadoCalificacion")
    private Long id;

    @Column(name = "Nombre_Calificacion", nullable = false, length = 50)
    private String nombreCalificacion;

    public EstadoCalificacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCalificacion() {
        return nombreCalificacion;
    }

    public void setNombreCalificacion(String nombreCalificacion) {
        this.nombreCalificacion = nombreCalificacion;
    }
}
