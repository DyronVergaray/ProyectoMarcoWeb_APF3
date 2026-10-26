package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.ContactoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a los contactos de las empresas.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): existsByCorreoEmpresarial evita repetir el correo empresarial al
 *    registrar.
 *  - RF-06 (Personalizar perfiles): existsByCorreoEmpresarial también evita que el administrador
 *    cambie el correo empresarial por uno que ya usa otra empresa.
 */
public interface ContactoEmpresaRepository extends JpaRepository<ContactoEmpresa, Long> {

    boolean existsByCorreoEmpresarial(String correoEmpresarial);
}
