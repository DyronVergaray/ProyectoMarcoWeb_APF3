// Dirección del backend de Spring Boot
const API_EMPRESAS = "http://localhost:8080/api/empresas/";

// Escribe un texto en un elemento (textContent: lo escrito por el usuario nunca se interpreta como HTML)
function poner(id, texto) {
    document.getElementById(id).textContent = texto;
}

function mostrarError(texto) {
    const caja = document.getElementById("mensajeError");
    caja.textContent = texto;
    caja.classList.remove("d-none");
}

// Redes sociales: si es un enlace http(s) se muestra como link, si no, como texto
function pintarRedes(redes) {
    const contenedor = document.getElementById("datoRedes");
    contenedor.textContent = "";

    if (!redes) {
        contenedor.textContent = "No registrado";
        return;
    }
    if (/^https?:\/\//i.test(redes)) {
        const enlace = document.createElement("a");
        enlace.href = redes;
        enlace.target = "_blank";
        enlace.rel = "noopener noreferrer";
        enlace.textContent = redes;
        contenedor.appendChild(enlace);
    } else {
        contenedor.textContent = redes;
    }
}

// Crea la fila de un usuario: iniciales, nombre, correo y rol
function crearFilaUsuario(u) {
    const fila = document.createElement("div");
    fila.className = "d-flex align-items-center";

    const avatar = document.createElement("div");
    avatar.className = "rounded-circle bg-custom-primary text-white d-flex align-items-center justify-content-center fw-bold flex-shrink-0";
    avatar.style.width = "55px";
    avatar.style.height = "55px";
    avatar.textContent = (u.nombreUsuario.charAt(0) + u.apellidoPUsuario.charAt(0)).toUpperCase();

    const info = document.createElement("div");
    info.className = "ms-3 min-w-0";

    const nombre = document.createElement("h6");
    nombre.className = "mb-0 fw-bold";
    nombre.textContent = u.nombreUsuario + " " + u.apellidoPUsuario + " " + u.apellidoMUsuario;

    const correo = document.createElement("small");
    correo.className = "text-secondary d-block text-break";
    correo.textContent = u.correo;

    const rol = document.createElement("span");
    rol.className = "badge bg-custom-primary mt-1";
    rol.textContent = u.rol;

    info.append(nombre, correo, rol);
    fila.append(avatar, info);
    return fila;
}

document.addEventListener("DOMContentLoaded", async () => {
    const idEmpresa = sessionStorage.getItem("idEmpresa");

    // Si no hay sesión de empresa iniciada, volver al login
    if (!idEmpresa) {
        window.location.href = "index.html";
        return;
    }

    try {
        const respuesta = await fetch(API_EMPRESAS + encodeURIComponent(idEmpresa));

        if (respuesta.status === 404) {
            sessionStorage.clear();
            window.location.href = "index.html";
            return;
        }
        if (!respuesta.ok) throw new Error("Error al obtener los datos");

        const empresa = await respuesta.json();

        poner("empresaNombre", empresa.nombreEmpresa);
        poner("empresaRubro", empresa.rubro);
        poner("datoNombre", empresa.nombreEmpresa);
        poner("datoRubro", empresa.rubro);
        poner("datoRuc", empresa.ruc);
        poner("datoTelefono", empresa.telefono);
        poner("datoCorreo", empresa.correoEmpresarial);
        pintarRedes(empresa.redesSociales);

        const lista = document.getElementById("listaUsuarios");
        lista.textContent = "";
        empresa.usuarios.forEach(u => lista.appendChild(crearFilaUsuario(u)));
        poner("totalUsuarios", empresa.usuarios.length);

    } catch (e) {
        poner("empresaNombre", "No disponible");
        mostrarError("No se pudo cargar la información de la empresa.");
    }
});
