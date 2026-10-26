// Dirección del backend de Spring Boot
const API_ESTUDIANTES = "http://localhost:8080/api/estudiantes/";

const listaPostulaciones = document.getElementById("listaPostulaciones");
const cajaMensaje = document.getElementById("mensaje");

// ---------- Utilidades ----------

function formatoSoles(numero) {
    return Number(numero).toLocaleString("es-PE", { style: "currency", currency: "PEN" });
}

// "2026-10-25" -> "25/10/2026" (sin pasar por Date para evitar desfases de zona horaria)
function formatoFecha(iso) {
    const [anio, mes, dia] = iso.split("-");
    return dia + "/" + mes + "/" + anio;
}

function iniciales(nombre) {
    const palabras = nombre.trim().split(/\s+/);
    return (palabras[0].charAt(0) + (palabras[1] ? palabras[1].charAt(0) : "")).toUpperCase();
}

function crear(etiqueta, clase, texto) {
    const el = document.createElement(etiqueta);
    if (clase) el.className = clase;
    if (texto !== undefined) el.textContent = texto; // textContent: nada de lo guardado se interpreta como HTML
    return el;
}

// ---------- Pintar ----------

function crearTarjeta(p) {
    const tarjeta = crear("article", "postulacion");
    tarjeta.appendChild(crear("div", "postulacion-avatar", iniciales(p.nombreEmpresa)));

    const cuerpo = crear("div", "postulacion-cuerpo");
    cuerpo.appendChild(crear("h2", "postulacion-puesto", p.puesto));
    cuerpo.appendChild(crear("div", "postulacion-empresa mb-2", p.nombreEmpresa));

    const chips = crear("div", "d-flex flex-wrap gap-2");
    chips.appendChild(crear("span", "chip", "Postulaste el " + formatoFecha(p.fechaPostulacion)));
    chips.appendChild(crear("span", "chip", formatoSoles(p.sueldo) + " · " + p.frecuenciaPagos));
    cuerpo.appendChild(chips);

    // Color según el estado: Activa, Aceptado o Rechazado
    const estado = crear("span", "estado estado-" + p.estado.toLowerCase(), p.estado);

    tarjeta.append(cuerpo, estado);
    return tarjeta;
}

function pintarResumen(lista) {
    const contar = nombre => lista.filter(p => p.estado === nombre).length;
    document.getElementById("totalTodas").textContent = lista.length;
    document.getElementById("totalActivas").textContent = contar("Activa");
    document.getElementById("totalAceptadas").textContent = contar("Aceptado");
    document.getElementById("totalRechazadas").textContent = contar("Rechazado");
}

function pintar(lista) {
    listaPostulaciones.textContent = "";
    pintarResumen(lista);

    if (lista.length === 0) {
        const vacio = crear("div", "tarjeta text-center text-secondary");
        vacio.append("Todavía no te has postulado a ninguna oferta. ");
        const enlace = crear("a", "", "Buscar trabajo");
        enlace.href = "buscar_trabajo.html";
        vacio.appendChild(enlace);
        listaPostulaciones.appendChild(vacio);
        return;
    }
    lista.forEach(p => listaPostulaciones.appendChild(crearTarjeta(p)));
}

// ---------- Inicio ----------

document.addEventListener("DOMContentLoaded", async () => {
    // Si no hay sesión de estudiante iniciada, volver al login
    const idEstudiante = sessionStorage.getItem("idEstudiante");
    if (!idEstudiante) {
        window.location.href = "index.html";
        return;
    }

    // Cerrar sesión: se borra el id guardado y se vuelve al login
    document.getElementById("btnCerrarSesion").addEventListener("click", () => {
        sessionStorage.removeItem("idEstudiante");
        window.location.href = "index.html";
    });

    try {
        const respuesta = await fetch(API_ESTUDIANTES + encodeURIComponent(idEstudiante) + "/postulaciones");
        if (!respuesta.ok) throw new Error("Error al obtener las postulaciones");
        pintar(await respuesta.json());
    } catch (e) {
        listaPostulaciones.textContent = "";
        cajaMensaje.textContent = "No se pudieron cargar tus postulaciones. Verifica que el servidor esté en marcha e intenta de nuevo.";
        cajaMensaje.classList.remove("d-none");
    }
});
