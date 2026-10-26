package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Empresa que publica ofertas de trabajo.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Se registra con su nombre, rubro y RUC, y enlazada a su
 *    contacto.
 *  - RF-06 (Personalizar perfiles): Sus datos, su contacto y sus usuarios forman el perfil de la
 *    empresa.
 */
@Entity
@Table(name = "Empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Empresa")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Id_ContactoEmpresa", nullable = false, unique = true)
    private ContactoEmpresa contactoEmpresa;

    @Column(name = "Nombre_Empresa", nullable = false, length = 150)
    private String nombreEmpresa;

    @Column(name = "Rubro", nullable = false, length = 100)
    private String rubro;

    @Column(name = "RUC", nullable = false, length = 11)
    private String ruc;

    public Empresa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactoEmpresa getContactoEmpresa() {
        return contactoEmpresa;
    }

    public void setContactoEmpresa(ContactoEmpresa contactoEmpresa) {
        this.contactoEmpresa = contactoEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
}
