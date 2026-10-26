package com.UTP.Avance3.Servicios;

import com.UTP.Avance3.DTO.ActualizarEstudianteRequest;
import com.UTP.Avance3.DTO.EstudianteResponse;
import com.UTP.Avance3.DTO.LoginRequest;
import com.UTP.Avance3.DTO.RegistroEstudianteRequest;
import com.UTP.Avance3.Entidades.Estudiante;
import com.UTP.Avance3.Entidades.PerfilEstudiante;
import com.UTP.Avance3.Repositorio.EstudianteRepository;
import com.UTP.Avance3.Repositorio.PerfilEstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * Lógica de negocio. El formato de los datos ya fue validado en los DTO,
 * aquí solo se aplican las reglas (correo/DNI únicos, credenciales) y se guarda.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Registra al postulante (perfil con contraseña y datos básicos)
 *    sin permitir correos ni DNI repetidos.
 *  - RF-02 (Inicio de sesión): Inicia sesión comparando correo y contraseña.
 *  - RF-06 (Personalizar perfiles): Consulta y actualiza los datos personales y de contacto del
 *    perfil.
 */
@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final PerfilEstudianteRepository perfilEstudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository,
                             PerfilEstudianteRepository perfilEstudianteRepository) {
        this.estudianteRepository = estudianteRepository;
        this.perfilEstudianteRepository = perfilEstudianteRepository;
    }

    /**
     * Registra un estudiante: primero guarda su perfil (con la contraseña)
     * y luego el estudiante enlazado a ese perfil.
     *
     * RF-01 (Registro de usuarios): Guarda los datos básicos del postulante y su contraseña
     *    (Perfil_Estudiante). El rol de postulante es implícito: se reconoce por estar en la tabla
     *    Estudiante, no se guarda en la tabla Rol.
     */
    @Transactional
    public EstudianteResponse registrarEstudiante(RegistroEstudianteRequest request) {
        String correo = request.correo().trim().toLowerCase();
        String dni = request.dni().trim();

        if (estudianteRepository.existsByCorreo(correo)) {
            throw new IllegalStateException("Ya existe una cuenta registrada con ese correo.");
        }
        if (estudianteRepository.existsByDni(dni)) {
            throw new IllegalStateException("Ya existe una cuenta registrada con ese DNI.");
        }

        PerfilEstudiante perfil = new PerfilEstudiante();
        perfil.setContrasena(request.contrasena());
        perfil = perfilEstudianteRepository.save(perfil);

        Estudiante estudiante = new Estudiante();
        estudiante.setPerfilEstudiante(perfil);
        estudiante.setNombreEstudiante(request.nombreEstudiante().trim());
        estudiante.setApellidoPEstudiante(request.apellidoPEstudiante().trim());
        estudiante.setApellidoMEstudiante(request.apellidoMEstudiante().trim());
        estudiante.setCorreo(correo);
        estudiante.setTelefono(request.telefono().trim());
        estudiante.setDni(dni);
        estudiante.setNivelEducativo(request.nivelEducativo().trim());
        estudiante.setDescripcion(request.descripcion().trim());

        return aRespuesta(estudianteRepository.save(estudiante));
    }

    /**
     * Inicia sesión con correo y contraseña. El mensaje de error es el mismo
     * para correo inexistente o contraseña incorrecta.
     *
     * RF-02 (Inicio de sesión): Valida las credenciales y rechaza las incorrectas. La sesión la
     *    guarda el navegador (sessionStorage);
     */
    @Transactional(readOnly = true)
    public EstudianteResponse iniciarSesion(LoginRequest request) {
        String correo = request.correo().trim().toLowerCase();

        Estudiante estudiante = estudianteRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos."));

        if (!estudiante.getPerfilEstudiante().getContrasena().equals(request.contrasena())) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos.");
        }
        return aRespuesta(estudiante);
    }

    /**
     * Devuelve los datos de un estudiante por su id.
     *
     * RF-06 (Personalizar perfiles): Datos que se muestran en el perfil del postulante.
     */
    @Transactional(readOnly = true)
    public EstudianteResponse obtenerPorId(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estudiante no encontrado."));
        return aRespuesta(estudiante);
    }

    /**
     * Actualiza los datos de un estudiante. Si cambia el correo o el DNI,
     * se verifica que no lo tenga otro estudiante.
     *
     * RF-06 (Personalizar perfiles): El postulante personaliza sus datos de perfil y de contacto.
     */
    @Transactional
    public EstudianteResponse actualizarEstudiante(Long id, ActualizarEstudianteRequest request) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estudiante no encontrado."));

        String correo = request.correo().trim().toLowerCase();
        String dni = request.dni().trim();

        if (!correo.equals(estudiante.getCorreo()) && estudianteRepository.existsByCorreo(correo)) {
            throw new IllegalStateException("Ya existe una cuenta registrada con ese correo.");
        }
        if (!dni.equals(estudiante.getDni()) && estudianteRepository.existsByDni(dni)) {
            throw new IllegalStateException("Ya existe una cuenta registrada con ese DNI.");
        }

        estudiante.setNombreEstudiante(request.nombreEstudiante().trim());
        estudiante.setApellidoPEstudiante(request.apellidoPEstudiante().trim());
        estudiante.setApellidoMEstudiante(request.apellidoMEstudiante().trim());
        estudiante.setCorreo(correo);
        estudiante.setTelefono(request.telefono().trim());
        estudiante.setDni(dni);
        estudiante.setNivelEducativo(request.nivelEducativo().trim());
        estudiante.setDescripcion(request.descripcion().trim());

        return aRespuesta(estudianteRepository.save(estudiante));
    }

    // Convierte la entidad en el DTO de respuesta (sin contraseña)
    private EstudianteResponse aRespuesta(Estudiante e) {
        return new EstudianteResponse(
                e.getId(),
                e.getNombreEstudiante(),
                e.getApellidoPEstudiante(),
                e.getApellidoMEstudiante(),
                e.getCorreo(),
                e.getTelefono(),
                e.getDni(),
                e.getNivelEducativo(),
                e.getDescripcion());
    }
}
