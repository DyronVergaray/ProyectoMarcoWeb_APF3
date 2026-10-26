package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a los roles.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Al registrar una empresa busca el rol Admin (sin importar
 *    mayúsculas) para asignarlo.
 *  - RF-03 (Control de acceso basado en roles): Es la fuente de los roles con los que se controla
 *    el acceso.
 */
public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombreRolIgnoreCase(String nombreRol);
}
