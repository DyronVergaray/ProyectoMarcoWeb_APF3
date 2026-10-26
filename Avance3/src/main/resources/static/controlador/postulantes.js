// Dirección del backend de Spring Boot
const API_USUARIOS = "http://localhost:8080/api/usuarios/";

let idUsuario = null;
let idOferta = null;
let postulantes = [];     // postulantes de la oferta
let pendiente = null;     // decisión que espera confirmación: { postulante, estado }

const tabla = document.getElementById("tablaPostulantes");
const cajaMensaje = document.getElementById("mensaje");
const botonConfirmar = document.getElementById("btnConfirmar");
const abrir = (id) => bootstrap.Modal.getOrCreateInstance(document.getElementById(id));

// ---------- Utilidades ----------

// "2026-10-25" -> "25/10/2026" (sin pasar por Date para evitar desfases de zona horaria)
function formatoFecha(iso) {
    const [anio, mes, dia] = iso.split("-");
    return dia + "/" + mes + "/" + anio;
}

function crear(etiqueta, clase, texto) {
    const el = document.createElement(etiqueta);
    if (clase) el.className = clase;
    if (texto !== undefined) el.textContent = texto; // textContent: nada de lo guardado se interpreta como HTML
    return el;
}

function nombreCompleto(p) {
    return p.nombreEstudiante + " " + p.apellidoPEstudiante + " " + p.apellidoMEstudiante;
}

function mostrarMensaje(texto, tipo) {
    cajaMensaje.className = "alert alert-" + tipo;
    cajaMensaje.textContent = texto;
    cajaMensaje.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

const CLASE_ESTADO_POSTULACION = {
    "Activa": "bg-custom-primary",
    "Aceptado": "bg-success",
    "Rechazado": "bg-danger"
};

const CLASE_ESTADO_OFERTA = {
    "Activa": "bg-success",
    "En Proceso": "bg-warning text-dark",
    "Finalizada": "bg-secondary"
};

// ---------- Tabla ----------

function pintarResumen() {
    const resumen = document.getElementById("resumen");
    resumen.textContent = "";
    const contar = nombre => postulantes.filter(p => p.estado === nombre).length;

    [["Total", postulantes.length, "bg-dark"],
     ["Activas", contar("Activa"), CLASE_ESTADO_POSTULACION["Activa"]],
     ["Aceptados", contar("Aceptado"), CLASE_ESTADO_POSTULACION["Aceptado"]],
     ["Rechazados", contar("Rechazado"), CLASE_ESTADO_POSTULACION["Rechazado"]]]
        .forEach(([texto, cantidad, clase]) => resumen.appendChild(crear("span", "badge " + clase, texto + ": " + cantidad)));
}

function pintarTabla() {
    tabla.textContent = "";
    pintarResumen();

    if (postulantes.length === 0) {
        const fila = crear("tr");
        const celda = crear("td", "text-center text-secondary py-4", "Todavía nadie se ha postulado a esta oferta.");
        celda.colSpan = 6;
        fila.appendChild(celda);
        tabla.appendChild(fila);
        return;
    }

    postulantes.forEach(p => {
        const fila = crear("tr");
        fila.appendChild(crear("td", "fw-medium", nombreCompleto(p)));
        fila.appendChild(crear("td", "text-break", p.correo));
        fila.appendChild(crear("td", "", p.nivelEducativo));
        fila.appendChild(crear("td", "", formatoFecha(p.fechaPostulacion)));

        const celdaEstado = crear("td");
        celdaEstado.appendChild(crear("span", "badge " + (CLASE_ESTADO_POSTULACION[p.estado] || "bg-secondary"), p.estado));
        fila.appendChild(celdaEstado);

        const acciones = crear("td", "text-end text-nowrap");

        const info = crear("button", "btn btn-sm btn-outline-secondary me-2", "Ver información");
        info.type = "button";
        info.addEventListener("click", () => verInformacion(p));
        acciones.appendChild(info);

        if (p.estado === "Activa") {
            const aceptar = crear("button", "btn btn-sm btn-outline-success me-2", "Aceptar");
            aceptar.type = "button";
            aceptar.addEventListener("click", () => pedirConfirmacion(p, "Aceptado", false));

            const rechazar = crear("button", "btn btn-sm btn-outline-danger", "Rechazar");
            rechazar.type = "button";
            rechazar.addEventListener("click", () => pedirConfirmacion(p, "Rechazado", false));

            acciones.append(aceptar, rechazar);
        } else {
            // Aceptado o Rechazado: decisión final, ya no hay botones para cambiarla
            acciones.appendChild(crear("span", "text-secondary small", "Decisión final"));
        }
        fila.appendChild(acciones);
        tabla.appendChild(fila);
    });
}

// ---------- Información del postulante ----------

function seccion(titulo) {
    return crear("h3", "fs-6 fw-bold text-custom-primary mt-4 mb-2", titulo);
}

// Solo se muestran como enlace las direcciones http(s)
function esEnlace(texto) {
    return /^https?:\/\//i.test(texto || "");
}

function pintarInformacion(d) {
    const cuerpo = document.getElementById("modalInfoCuerpo");
    cuerpo.textContent = "";

    cuerpo.appendChild(crear("h2", "fs-4 fw-bold mb-3", d.nombreEstudiante + " " + d.apellidoPEstudiante + " " + d.apellidoMEstudiante));

    const dl = crear("dl", "row mb-0");
    [["Correo", d.correo], ["Teléfono", d.telefono], ["Nivel educativo", d.nivelEducativo]].forEach(([etiqueta, valor]) => {
        dl.appendChild(crear("dt", "col-sm-4", etiqueta));
        dl.appendChild(crear("dd", "col-sm-8 text-break", valor));
    });
    cuerpo.appendChild(dl);

    cuerpo.appendChild(seccion("Descripción"));
    cuerpo.appendChild(crear("p", "mb-0" + (d.descripcion ? "" : " text-secondary"), d.descripcion || "No ha agregado una descripción."));

    cuerpo.appendChild(seccion("Títulos"));
    if (d.titulos.length === 0) {
        cuerpo.appendChild(crear("p", "text-secondary mb-0", "No ha registrado títulos."));
    } else {
        const ul = crear("ul", "mb-0");
        d.titulos.forEach(t => {
            const li = crear("li", "", t.nombreTitulo + " — " + t.entidad);
            if (esEnlace(t.descargable)) {
                li.append(" · ");
                const enlace = crear("a", "", "Ver documento");
                enlace.href = t.descargable;
                enlace.target = "_blank";
                enlace.rel = "noopener noreferrer";
                li.appendChild(enlace);
            }
            ul.appendChild(li);
        });
        cuerpo.appendChild(ul);
    }

    cuerpo.appendChild(seccion("Experiencia laboral"));
    if (d.experiencias.length === 0) {
        cuerpo.appendChild(crear("p", "text-secondary mb-0", "No ha registrado experiencia laboral."));
    } else {
        const ul = crear("ul", "mb-0");
        d.experiencias.forEach(x => ul.appendChild(crear("li", "", x.nombrePuesto + " en " + x.entidad + " (" + x.periodo + ")")));
        cuerpo.appendChild(ul);
    }
}

async function verInformacion(p) {
    const cuerpo = document.getElementById("modalInfoCuerpo");
    const aceptar = document.getElementById("btnAceptarInfo");
    const rechazar = document.getElementById("btnRechazarInfo");

    cuerpo.textContent = "Cargando información...";
    // Solo se puede decidir mientras la postulación sigue Activa
    const activa = p.estado === "Activa";
    aceptar.classList.toggle("d-none", !activa);
    rechazar.classList.toggle("d-none", !activa);
    aceptar.onclick = () => pedirConfirmacion(p, "Aceptado", true);
    rechazar.onclick = () => pedirConfirmacion(p, "Rechazado", true);

    abrir("modalInfo").show();

    try {
        const respuesta = await fetch(API_USUARIOS + encodeURIComponent(idUsuario) + "/postulaciones/" + encodeURIComponent(p.idPostulacion) + "/postulante");
        const datos = await respuesta.json().catch(() => ({}));
        if (!respuesta.ok) throw new Error(datos.mensaje || "Error al obtener la información");
        pintarInformacion(datos);
    } catch (e) {
        cuerpo.textContent = "No se pudo cargar la información del postulante.";
    }
}

// ---------- Cambiar el estado ----------

// desdeInfo: la decisión se tomó dentro de la ventana de información, que hay que cerrar primero
function pedirConfirmacion(p, estado, desdeInfo) {
    pendiente = { postulante: p, estado: estado };

    const verbo = estado === "Aceptado" ? "aceptar" : "rechazar";
    document.getElementById("confirmarTexto").textContent = "¿Seguro que quieres " + verbo + " a " + nombreCompleto(p) + "?";
    botonConfirmar.textContent = estado === "Aceptado" ? "Aceptar postulante" : "Rechazar postulante";
    botonConfirmar.className = "btn " + (estado === "Aceptado" ? "btn-success" : "btn-danger");
    botonConfirmar.disabled = false;

    if (desdeInfo) {
        document.getElementById("modalInfo").addEventListener("hidden.bs.modal", () => abrir("modalConfirmar").show(), { once: true });
        abrir("modalInfo").hide();
    } else {
        abrir("modalConfirmar").show();
    }
}

async function confirmarCambio() {
    if (!pendiente) return;
    const { postulante, estado } = pendiente;

    botonConfirmar.disabled = true;
    try {
        const url = API_USUARIOS + encodeURIComponent(idUsuario) + "/postulaciones/" + encodeURIComponent(postulante.idPostulacion) + "/estado";
        const respuesta = await fetch(url, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ estado: estado })
        });
        const cuerpo = await respuesta.json().catch(() => ({}));

        abrir("modalConfirmar").hide();
        if (respuesta.ok) {
            postulantes = postulantes.map(p => p.idPostulacion === cuerpo.idPostulacion ? cuerpo : p);
            pintarTabla();
            mostrarMensaje(nombreCompleto(cuerpo) + " fue marcado como " + cuerpo.estado + ".", "success");
        } else {
            mostrarMensaje(cuerpo.mensaje || "No se pudo cambiar el estado.", "danger");
            // 409: alguien ya había decidido; se vuelve a cargar la lista para ver el estado real
            if (respuesta.status === 409) cargar();
        }
    } catch (e) {
        abrir("modalConfirmar").hide();
        mostrarMensaje("No se pudo conectar con el servidor.", "danger");
    }
    pendiente = null;
}

// ---------- Inicio ----------

async function cargar() {
    try {
        const respuesta = await fetch(API_USUARIOS + encodeURIComponent(idUsuario) + "/ofertas/" + encodeURIComponent(idOferta) + "/postulantes");
        const datos = await respuesta.json().catch(() => ({}));
        if (!respuesta.ok) throw new Error(datos.mensaje || "Error al obtener los postulantes");

        postulantes = datos.postulantes;
        document.getElementById("titulo").textContent = "Postulantes de " + datos.puesto;

        const badge = document.getElementById("estadoOferta");
        badge.textContent = "Oferta " + datos.estadoOferta;
        badge.className = "badge " + (CLASE_ESTADO_OFERTA[datos.estadoOferta] || "bg-secondary");

        pintarTabla();
    } catch (e) {
        tabla.textContent = "";
        document.getElementById("resumen").textContent = "";
        mostrarMensaje(e.message || "No se pudieron cargar los postulantes.", "danger");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    idUsuario = sessionStorage.getItem("idUsuario");

    // Si no hay sesión iniciada, volver al login
    if (!idUsuario) {
        window.location.href = "index.html";
        return;
    }
    // Solo el administrador revisa postulantes (el backend también lo verifica)
    if ((sessionStorage.getItem("rolUsuario") || "").toLowerCase() !== "admin") {
        window.location.href = "perfil.html";
        return;
    }

    // La oferta viene en la dirección: postulantes.html?idOferta=3
    idOferta = new URLSearchParams(window.location.search).get("idOferta");
    if (!idOferta || !/^\d+$/.test(idOferta)) {
        window.location.href = "mis-vacantes.html";
        return;
    }

    botonConfirmar.addEventListener("click", confirmarCambio);
    cargar();
});
