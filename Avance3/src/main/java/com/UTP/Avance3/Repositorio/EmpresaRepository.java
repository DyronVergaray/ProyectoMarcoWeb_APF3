package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a las empresas.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): existsByRuc evita registrar dos veces la misma empresa.
 *  - RF-06 (Personalizar perfiles): existsByRuc también evita que el administrador cambie el RUC de
 *    su empresa por uno que ya usa otra.
 */
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    boolean existsByRuc(String ruc);
}
