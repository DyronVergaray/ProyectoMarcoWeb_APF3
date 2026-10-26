package com.UTP.Avance3.Entidades;

import jakarta.persistence.*;

/**
 * Datos de contacto de una empresa: teléfono, correo empresarial y redes sociales.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Se guarda junto con la empresa al registrarla: una empresa tiene
 *    un solo contacto.
 *  - RF-06 (Personalizar perfiles): Es el bloque Contacto del perfil de la empresa.
 */
@Entity
@Table(name = "Contacto_Empresa")
public class ContactoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_ContactoEmpresa")
    private Long id;

    @Column(name = "Telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "Correo_Empresarial", nullable = false, length = 150)
    private String correoEmpresarial;

    @Column(name = "Redes_Sociales", nullable = false, length = 255)
    private String redesSociales;

    public ContactoEmpresa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoEmpresarial() {
        return correoEmpresarial;
    }

    public void setCorreoEmpresarial(String correoEmpresarial) {
        this.correoEmpresarial = correoEmpresarial;
    }

    public String getRedesSociales() {
        return redesSociales;
    }

    public void setRedesSociales(String redesSociales) {
        this.redesSociales = redesSociales;
    }
}
