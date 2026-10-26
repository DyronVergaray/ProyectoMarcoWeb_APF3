package com.UTP.Avance3.Repositorio;

import com.UTP.Avance3.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a los usuarios de empresa.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): existsByCorreo y existsByDni evitan repetir usuarios al
 *    registrar una empresa.
 *  - RF-02 (Inicio de sesión): findByCorreo busca al usuario para iniciar sesión.
 *  - RF-03 (Control de acceso basado en roles): Se usa para conocer el rol y la empresa del usuario
 *    que ejecuta una acción.
 *  - RF-06 (Personalizar perfiles): findByEmpresaIdOrderByIdAsc lista los usuarios de una empresa
 *    para su perfil.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreo(String correo);

    boolean existsByDni(String dni);

    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findByEmpresaIdOrderByIdAsc(Long idEmpresa);
}
