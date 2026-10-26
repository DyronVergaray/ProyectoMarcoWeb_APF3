package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.PerfilEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a la contraseña de los estudiantes.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Guarda la contraseña al registrar al postulante.
 *  - RF-02 (Inicio de sesión): Su dato se compara al iniciar sesión.
 */
public interface PerfilEstudianteRepository extends JpaRepository<PerfilEstudiante, Long> {
}
