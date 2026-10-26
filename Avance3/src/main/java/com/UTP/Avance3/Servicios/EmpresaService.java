package com.UTP.Avance3.Servicios;

import com.UTP.Avance3.DTO.EmpresaResponse;
import com.UTP.Avance3.DTO.ModificarEmpresaRequest;
import com.UTP.Avance3.DTO.PerfilEmpresaResponse;
import com.UTP.Avance3.DTO.RegistroEmpresaRequest;
import com.UTP.Avance3.Entidades.ContactoEmpresa;
import com.UTP.Avance3.Entidades.Credenciales;
import com.UTP.Avance3.Entidades.Empresa;
import com.UTP.Avance3.Entidades.Rol;
import com.UTP.Avance3.Entidades.Usuario;
import com.UTP.Avance3.Repositorio.ContactoEmpresaRepository;
import com.UTP.Avance3.Repositorio.CredencialesRepository;
import com.UTP.Avance3.Repositorio.EmpresaRepository;
import com.UTP.Avance3.Repositorio.RolRepository;
import com.UTP.Avance3.Repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * Lógica de negocio. El formato de los datos ya fue validado en los DTO,
 * aquí solo se aplican las reglas (RUC, correos y DNI únicos) y se guarda.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Registra la empresa junto con su contacto y su usuario
 *    administrador, validando que RUC, correos y DNI no estén repetidos.
 *  - RF-03 (Control de acceso basado en roles): Al usuario que se crea con la empresa se le asigna
 *    el rol Admin.
 *  - RF-06 (Personalizar perfiles): Arma el perfil de la empresa (datos, contacto y usuarios con su
 *    rol) y permite que el administrador modifique los datos y el contacto de su empresa.
 */
@Service
public class EmpresaService {

    // Nombre del rol que se asigna al usuario que crea la empresa (tabla Rol)
    private static final String ROL_ADMIN = Rol.ADMIN;

    private final EmpresaRepository empresaRepository;
    private final ContactoEmpresaRepository contactoEmpresaRepository;
    private final CredencialesRepository credencialesRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public EmpresaService(EmpresaRepository empresaRepository,
                          ContactoEmpresaRepository contactoEmpresaRepository,
                          CredencialesRepository credencialesRepository,
                          RolRepository rolRepository,
                          UsuarioRepository usuarioRepository,
                          UsuarioService usuarioService) {
        this.empresaRepository = empresaRepository;
        this.contactoEmpresaRepository = contactoEmpresaRepository;
        this.credencialesRepository = credencialesRepository;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Registra una empresa en un solo paso (si algo falla no se guarda nada):
     * 1) contacto de la empresa, 2) empresa, 3) credenciales (contraseña),
     * 4) usuario administrador enlazado a la empresa, el rol y las credenciales.
     *
     * RF-01 (Registro de usuarios): Guarda el contacto, la empresa, las credenciales y el usuario,
     *    en ese orden, dentro de una sola transacción.
     * RF-03 (Control de acceso basado en roles): El usuario queda con el rol Admin y enlazado a su
     *    empresa.
     */
    @Transactional
    public EmpresaResponse registrarEmpresa(RegistroEmpresaRequest request) {
        String ruc = request.ruc().trim();
        String correoEmpresarial = request.correoEmpresarial().trim().toLowerCase();
        String correoAdmin = request.correo().trim().toLowerCase();
        String dni = request.dni().trim();

        if (empresaRepository.existsByRuc(ruc)) {
            throw new IllegalStateException("Ya existe una empresa registrada con ese RUC.");
        }
        if (contactoEmpresaRepository.existsByCorreoEmpresarial(correoEmpresarial)) {
            throw new IllegalStateException("Ya existe una empresa registrada con ese correo empresarial.");
        }
        if (usuarioRepository.existsByCorreo(correoAdmin)) {
            throw new IllegalStateException("Ya existe un usuario registrado con ese correo.");
        }
        if (usuarioRepository.existsByDni(dni)) {
            throw new IllegalStateException("Ya existe un usuario registrado con ese DNI.");
        }

        // 1) Contacto de la empresa
        ContactoEmpresa contacto = new ContactoEmpresa();
        contacto.setTelefono(request.telefono().trim());
        contacto.setCorreoEmpresarial(correoEmpresarial);
        // La columna es NOT NULL: si no hay redes sociales se guarda vacío
        contacto.setRedesSociales(request.redesSociales() == null ? "" : request.redesSociales().trim());
        contacto = contactoEmpresaRepository.save(contacto);

        // 2) Empresa
        Empresa empresa = new Empresa();
        empresa.setContactoEmpresa(contacto);
        empresa.setNombreEmpresa(request.nombreEmpresa().trim());
        empresa.setRubro(request.rubro().trim());
        empresa.setRuc(ruc);
        empresa = empresaRepository.save(empresa);

        // 3) Credenciales (contraseña del administrador)
        Credenciales credenciales = new Credenciales();
        credenciales.setContrasena(request.contrasena());
        credenciales = credencialesRepository.save(credenciales);

        // 4) Usuario administrador con el rol "Admin" (se crea en la tabla Rol si aún no existe)
        Rol rolAdmin = rolRepository.findByNombreRolIgnoreCase(ROL_ADMIN)
                .orElseGet(() -> {
                    Rol nuevo = new Rol();
                    nuevo.setNombreRol(ROL_ADMIN);
                    return rolRepository.save(nuevo);
                });

        Usuario usuario = new Usuario();
        usuario.setEmpresa(empresa);
        usuario.setRol(rolAdmin);
        usuario.setCredenciales(credenciales);
        usuario.setNombreUsuario(request.nombreUsuario().trim());
        usuario.setApellidoPUsuario(request.apellidoPUsuario().trim());
        usuario.setApellidoMUsuario(request.apellidoMUsuario().trim());
        usuario.setCorreo(correoAdmin);
        usuario.setDni(dni);
        usuario = usuarioRepository.save(usuario);

        return aRespuesta(empresa, contacto, usuario, rolAdmin);
    }

    /**
     * Devuelve los datos de la empresa y todos sus usuarios con su rol (para perfil.html).
     *
     * RF-06 (Personalizar perfiles): Perfil de la empresa con su contacto y sus usuarios. Es lo
     *    que muestra perfil.html y lo que configuracion.html carga para editar.
     */
    @Transactional(readOnly = true)
    public PerfilEmpresaResponse obtenerPerfil(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Empresa no encontrada."));
        return aPerfil(empresa);
    }

    /**
     * El administrador modifica los datos y el contacto de su empresa (configuracion.html).
     *
     * RF-06 (Personalizar perfiles): actualiza nombre, rubro y RUC de la empresa y teléfono, correo
     *    empresarial y redes sociales de su contacto, todo en una sola transacción.
     * RF-03 (Control de acceso basado en roles): solo un usuario con rol Admin puede hacerlo y solo
     *    sobre la empresa a la que pertenece: la empresa se toma del usuario, no de la URL, así que
     *    no se puede editar la de otra. Por ahora el id del usuario llega en la URL; con JWT o
     *    Spring Security saldría del token.
     */
    @Transactional
    public PerfilEmpresaResponse modificarEmpresa(Long idUsuario, ModificarEmpresaRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado."));
        if (!Rol.ADMIN.equalsIgnoreCase(usuario.getRol().getNombreRol())) {
            throw new SecurityException("Solo un administrador puede modificar los datos de la empresa.");
        }

        Empresa empresa = usuario.getEmpresa();
        ContactoEmpresa contacto = empresa.getContactoEmpresa();

        String ruc = request.ruc().trim();
        String correoEmpresarial = request.correoEmpresarial().trim().toLowerCase();

        // Solo se comprueba si el valor cambió: el RUC y el correo actuales ya son de esta empresa
        if (!ruc.equals(empresa.getRuc()) && empresaRepository.existsByRuc(ruc)) {
            throw new IllegalStateException("Ya existe una empresa registrada con ese RUC.");
        }
        if (!correoEmpresarial.equals(contacto.getCorreoEmpresarial())
                && contactoEmpresaRepository.existsByCorreoEmpresarial(correoEmpresarial)) {
            throw new IllegalStateException("Ya existe una empresa registrada con ese correo empresarial.");
        }

        // Contacto de la empresa
        contacto.setTelefono(request.telefono().trim());
        contacto.setCorreoEmpresarial(correoEmpresarial);
        // La columna es NOT NULL: si no hay redes sociales se guarda vacío
        contacto.setRedesSociales(request.redesSociales() == null ? "" : request.redesSociales().trim());
        contactoEmpresaRepository.save(contacto);

        // Datos de la empresa
        empresa.setNombreEmpresa(request.nombreEmpresa().trim());
        empresa.setRubro(request.rubro().trim());
        empresa.setRuc(ruc);
        empresa = empresaRepository.save(empresa);

        return aPerfil(empresa);
    }

    // Arma el perfil de la empresa con su contacto y sus usuarios (sin contraseñas)
    private PerfilEmpresaResponse aPerfil(Empresa empresa) {
        ContactoEmpresa contacto = empresa.getContactoEmpresa();
        return new PerfilEmpresaResponse(
                empresa.getId(),
                empresa.getNombreEmpresa(),
                empresa.getRubro(),
                empresa.getRuc(),
                contacto.getTelefono(),
                contacto.getCorreoEmpresarial(),
                contacto.getRedesSociales(),
                usuarioService.listarPorEmpresa(empresa.getId()));
    }

    // Convierte las entidades en el DTO de respuesta (sin contraseña)
    private EmpresaResponse aRespuesta(Empresa e, ContactoEmpresa c, Usuario u, Rol r) {
        return new EmpresaResponse(
                e.getId(),
                e.getNombreEmpresa(),
                e.getRubro(),
                e.getRuc(),
                c.getTelefono(),
                c.getCorreoEmpresarial(),
                c.getRedesSociales(),
                u.getId(),
                u.getNombreUsuario() + " " + u.getApellidoPUsuario() + " " + u.getApellidoMUsuario(),
                u.getCorreo(),
                r.getNombreRol());
    }
}
