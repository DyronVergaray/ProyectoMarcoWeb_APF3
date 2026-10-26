package com.UTP.Avance3.Config;

import com.UTP.Avance3.Entidades.EstadoPostulacion;
import com.UTP.Avance3.Entidades.RequisitoOferta;
import com.UTP.Avance3.Entidades.Rol;
import com.UTP.Avance3.Repositorio.EstadoPostulacionRepository;
import com.UTP.Avance3.Repositorio.RequisitoOfertaRepository;
import com.UTP.Avance3.Repositorio.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Se ejecuta cada vez que arranca la aplicación (después de que Hibernate crea las tablas)
 * y deja listos los datos base. Es seguro repetirlo: solo agrega lo que todavía no existe.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): Crea el rol "Admin" en la tabla Rol, que se asigna al usuario
 *    que registra una empresa.
 *  - RF-03 (Control de acceso basado en roles): Deja cargado el rol "Admin" desde el arranque; con
 *    él se decide qué puede hacer cada usuario de empresa.
 *  - RF-04 (Anuncios de trabajo): Crea los estados de postulación (Activa, Rechazado, Aceptado),
 *    que son las fases por las que pasa un postulante.
 *  - RF-05 (Filtros para búsqueda de trabajo): Crea los requisitos que la empresa elige al publicar
 *    una oferta y que el postulante consulta en cada anuncio.
 */
@Component
public class DatosIniciales implements CommandLineRunner {

    // Requisitos que el administrador puede seleccionar al crear una oferta
    private static final List<String> REQUISITOS = List.of(
            "Estudiante universitario o técnico en curso",
            "Egresado o bachiller",
            "Título profesional",
            "Experiencia mínima de 6 meses",
            "Experiencia mínima de 1 año",
            "Inglés intermedio",
            "Inglés avanzado",
            "Manejo de Microsoft Office",
            "Excel avanzado",
            "Conocimientos de programación",
            "Trabajo en equipo",
            "Comunicación efectiva",
            "Proactividad",
            "Disponibilidad inmediata",
            "Disponibilidad a tiempo completo",
            "Disponibilidad para viajar",
            "Licencia de conducir");

    private static final List<String> ESTADOS_POSTULACION = List.of(
            EstadoPostulacion.ACTIVA,
            EstadoPostulacion.RECHAZADO,
            EstadoPostulacion.ACEPTADO);

    private final RolRepository rolRepository;
    private final RequisitoOfertaRepository requisitoOfertaRepository;
    private final EstadoPostulacionRepository estadoPostulacionRepository;

    public DatosIniciales(RolRepository rolRepository,
                          RequisitoOfertaRepository requisitoOfertaRepository,
                          EstadoPostulacionRepository estadoPostulacionRepository) {
        this.rolRepository = rolRepository;
        this.requisitoOfertaRepository = requisitoOfertaRepository;
        this.estadoPostulacionRepository = estadoPostulacionRepository;
    }

    @Override
    public void run(String... args) {
        // RF-01 y RF-03: rol de administrador de empresa
        if (rolRepository.findByNombreRolIgnoreCase(Rol.ADMIN).isEmpty()) {
            Rol admin = new Rol();
            admin.setNombreRol(Rol.ADMIN);
            rolRepository.save(admin);
        }

        // RF-04 y RF-05: requisitos que se pueden elegir al crear o modificar una oferta
        for (String nombre : REQUISITOS) {
            if (!requisitoOfertaRepository.existsByNombreRequisito(nombre)) {
                RequisitoOferta requisito = new RequisitoOferta();
                requisito.setNombreRequisito(nombre);
                requisitoOfertaRepository.save(requisito);
            }
        }

        // RF-04: fases de una postulación (toda postulación nueva empieza en Activa)
        for (String nombre : ESTADOS_POSTULACION) {
            if (!estadoPostulacionRepository.existsByNombreEstado(nombre)) {
                EstadoPostulacion estado = new EstadoPostulacion();
                estado.setNombreEstado(nombre);
                estadoPostulacionRepository.save(estado);
            }
        }
    }
}
