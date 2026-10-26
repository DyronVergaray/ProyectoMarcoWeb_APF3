package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a los datos de los estudiantes.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): existsByCorreo y existsByDni evitan registrar cuentas repetidas.
 *  - RF-02 (Inicio de sesión): findByCorreo busca la cuenta para iniciar sesión.
 */
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByDni(String dni);
}
