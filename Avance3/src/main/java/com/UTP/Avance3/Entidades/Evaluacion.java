package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Evaluacion")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Evaluacion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Oferta", nullable = false)
    private Oferta oferta;

    @Column(name = "Fecha_Inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "Fecha_Fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "Pregunta_1", nullable = false, length = 150)
    private String pregunta1;

    @Column(name = "Pregunta_2", nullable = false, length = 150)
    private String pregunta2;

    @Column(name = "Pregunta_3", nullable = false, length = 150)
    private String pregunta3;

    @Column(name = "Pregunta_4", nullable = false, length = 150)
    private String pregunta4;

    public Evaluacion() {
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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(String pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public String getPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(String pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public String getPregunta3() {
        return pregunta3;
    }

    public void setPregunta3(String pregunta3) {
        this.pregunta3 = pregunta3;
    }

    public String getPregunta4() {
        return pregunta4;
    }

    public void setPregunta4(String pregunta4) {
        this.pregunta4 = pregunta4;
    }
}
