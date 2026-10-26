package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.Credenciales;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a la contraseña de los usuarios de empresa.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Guarda la contraseña al registrar la empresa.
 *  - RF-02 (Inicio de sesión): Su dato se compara al iniciar sesión.
 */
public interface CredencialesRepository extends JpaRepository<Credenciales, Long> {
}
