package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Notas")
public class Notas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Notas")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Evaluacion", nullable = false)
    private Evaluacion evaluacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_Postulacion", nullable = false)
    private Postulacion postulacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_EstadoCalificacion", nullable = false)
    private EstadoCalificacion estadoCalificacion;

    @Column(name = "Nota", nullable = false, precision = 5, scale = 2)
    private BigDecimal nota;

    @Column(name = "Respuesta_1", nullable = false, length = 150)
    private String respuesta1;

    @Column(name = "Respuesta_2", nullable = false, length = 150)
    private String respuesta2;

    @Column(name = "Respuesta_3", nullable = false, length = 150)
    private String respuesta3;

    @Column(name = "Respuesta_4", nullable = false, length = 150)
    private String respuesta4;

    public Notas() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
    }

    public EstadoCalificacion getEstadoCalificacion() {
        return estadoCalificacion;
    }

    public void setEstadoCalificacion(EstadoCalificacion estadoCalificacion) {
        this.estadoCalificacion = estadoCalificacion;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public String getRespuesta1() {
        return respuesta1;
    }

    public void setRespuesta1(String respuesta1) {
        this.respuesta1 = respuesta1;
    }

    public String getRespuesta2() {
        return respuesta2;
    }

    public void setRespuesta2(String respuesta2) {
        this.respuesta2 = respuesta2;
    }

    public String getRespuesta3() {
        return respuesta3;
    }

    public void setRespuesta3(String respuesta3) {
        this.respuesta3 = respuesta3;
    }

    public String getRespuesta4() {
        return respuesta4;
    }

    public void setRespuesta4(String respuesta4) {
        this.respuesta4 = respuesta4;
    }
}
