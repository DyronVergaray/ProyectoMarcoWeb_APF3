package com.UTP.Avance3.Servicios;

import com.UTP.Avance3.DTO.LoginRequest;
import com.UTP.Avance3.DTO.UsuarioResponse;
import com.UTP.Avance3.Entidades.Usuario;
import com.UTP.Avance3.Repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de los usuarios de empresa.
 *
 * Requerimientos funcionales:
 *  - RF-02 (Inicio de sesión): Inicia sesión comparando correo y contraseña guardada en
 *    Credenciales.
 *  - RF-03 (Control de acceso basado en roles): Devuelve el rol de cada usuario, que es la base
 *    para restringir funciones.
 *  - RF-06 (Personalizar perfiles): Lista los usuarios de una empresa para mostrarlos en su perfil.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Inicia sesión con correo y contraseña. El mensaje de error es el mismo
     * para correo inexistente o contraseña incorrecta.
     *
     * RF-02 (Inicio de sesión): Valida las credenciales y rechaza las incorrectas. La sesión la
     *    guarda el navegador (sessionStorage); todavía no se genera un JWT.
     * RF-03 (Control de acceso basado en roles): La respuesta incluye el rol, que el navegador usa
     *    para mostrar u ocultar opciones.
     */
    @Transactional(readOnly = true)
    public UsuarioResponse iniciarSesion(LoginRequest request) {
        String correo = request.correo().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos."));

        if (!usuario.getCredenciales().getContrasena().equals(request.contrasena())) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos.");
        }
        return aRespuesta(usuario);
    }

    /**
     * Devuelve todos los usuarios de una empresa, con su rol.
     *
     * RF-06 (Personalizar perfiles): Usuarios de la empresa con su rol, para el perfil de la
     *    empresa.
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarPorEmpresa(Long idEmpresa) {
        return usuarioRepository.findByEmpresaIdOrderByIdAsc(idEmpresa).stream()
                .map(this::aRespuesta)
                .toList();
    }

    private UsuarioResponse aRespuesta(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNombreUsuario(),
                u.getApellidoPUsuario(),
                u.getApellidoMUsuario(),
                u.getCorreo(),
                u.getRol().getNombreRol(),
                u.getEmpresa().getId(),
                u.getEmpresa().getNombreEmpresa());
    }
}
