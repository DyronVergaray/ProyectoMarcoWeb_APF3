// Dirección del backend de Spring Boot
const API_ESTUDIANTES = "http://localhost:8080/api/estudiantes/";

const idEstudiante = sessionStorage.getItem("idEstudiante");

// Si no hay sesión iniciada, volver al login
if (!idEstudiante) {
    window.location.href = "index.html";
}

function urlApi(recurso, extra) {
    return API_ESTUDIANTES + encodeURIComponent(idEstudiante) + "/" + recurso + (extra ? "/" + extra : "");
}

function mostrarMensaje(caja, texto, tipo, lista) {
    caja.className = "alert alert-" + tipo;
    caja.textContent = texto;

    // Lista de errores de validación enviados por el backend
    if (lista && lista.length > 0) {
        const ul = document.createElement("ul");
        ul.className = "mb-0 mt-2";
        lista.forEach(mensaje => {
            const li = document.createElement("li");
            li.textContent = mensaje;
            ul.appendChild(li);
        });
        caja.appendChild(ul);
    }
}

// Solo se muestran enlaces http/https (evita enlaces tipo javascript:)
function esEnlaceSeguro(url) {
    return /^https?:\/\//i.test(url);
}

function filaVacia(texto) {
    const li = document.createElement("li");
    li.className = "list-group-item text-muted";
    li.textContent = texto;
    return li;
}

// Crea un elemento de la lista con título, detalle y botón Eliminar
function crearItem(titulo, detalle, enlace, alEliminar) {
    const li = document.createElement("li");
    li.className = "list-group-item d-flex justify-content-between align-items-start gap-3";

    const info = document.createElement("div");
    const t = document.createElement("div");
    t.className = "fw-bold";
    t.textContent = titulo;
    const d = document.createElement("small");
    d.className = "text-muted";
    d.textContent = detalle;
    info.append(t, d);

    if (enlace && esEnlaceSeguro(enlace)) {
        const a = document.createElement("a");
        a.href = enlace;
        a.target = "_blank";
        a.rel = "noopener noreferrer";
        a.textContent = "Descargar";
        a.className = "d-block small";
        info.appendChild(a);
    }

    const boton = document.createElement("button");
    boton.type = "button";
    boton.className = "btn btn-outline-danger btn-sm";
    boton.textContent = "Eliminar";
    boton.addEventListener("click", alEliminar);

    li.append(info, boton);
    return li;
}

async function cargarTitulos() {
    const lista = document.getElementById("listaTitulos");
    try {
        const respuesta = await fetch(urlApi("titulos"));
        if (!respuesta.ok) throw new Error();
        const titulos = await respuesta.json();

        lista.replaceChildren();
        if (titulos.length === 0) lista.appendChild(filaVacia("Aún no has agregado títulos."));
        titulos.forEach(t => {
            lista.appendChild(crearItem(t.nombreTitulo, t.entidad, t.descargable,
                () => eliminar("titulos", t.id, "este título", cargarTitulos)));
        });
    } catch (e) {
        lista.replaceChildren(filaVacia("No se pudieron cargar los títulos."));
    }
}

async function cargarExperiencias() {
    const lista = document.getElementById("listaExperiencias");
    try {
        const respuesta = await fetch(urlApi("experiencias"));
        if (!respuesta.ok) throw new Error();
        const experiencias = await respuesta.json();

        lista.replaceChildren();
        if (experiencias.length === 0) lista.appendChild(filaVacia("Aún no has agregado experiencias."));
        experiencias.forEach(x => {
            lista.appendChild(crearItem(x.nombrePuesto, x.entidad + " · " + x.periodo, null,
                () => eliminar("experiencias", x.id, "esta experiencia", cargarExperiencias)));
        });
    } catch (e) {
        lista.replaceChildren(filaVacia("No se pudieron cargar las experiencias."));
    }
}

async function eliminar(recurso, id, descripcion, recargar) {
    if (!confirm("¿Seguro que deseas eliminar " + descripcion + "?")) return;
    try {
        const respuesta = await fetch(urlApi(recurso, id), { method: "DELETE" });
        if (!respuesta.ok) throw new Error();
        recargar();
    } catch (e) {
        alert("No se pudo eliminar.");
    }
}

// Envía el formulario al backend y, si todo sale bien, limpia el formulario y recarga la lista
async function agregar(formulario, recurso, datos, caja, recargar) {
    if (!formulario.checkValidity()) {
        formulario.reportValidity();
        return;
    }

    const boton = formulario.querySelector("button[type=submit]");
    boton.disabled = true;
    try {
        const respuesta = await fetch(urlApi(recurso), {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });
        const cuerpo = await respuesta.json().catch(() => ({}));

        if (respuesta.ok) {
            formulario.reset();
            mostrarMensaje(caja, "Agregado correctamente.", "success");
            setTimeout(() => caja.classList.add("d-none"), 3000);
            recargar();
        } else {
            mostrarMensaje(caja, cuerpo.mensaje || "No se pudo agregar.", "danger",
                cuerpo.errores ? Object.values(cuerpo.errores) : null);
        }
    } catch (e) {
        mostrarMensaje(caja, "No se pudo conectar con el servidor.", "danger");
    }
    boton.disabled = false;
}

document.addEventListener("DOMContentLoaded", () => {
    if (!idEstudiante) return;

    document.getElementById("btnCerrarSesion").addEventListener("click", () => {
        sessionStorage.removeItem("idEstudiante");
        window.location.href = "index.html";
    });

    const formTitulo = document.getElementById("formTitulo");
    formTitulo.addEventListener("submit", evento => {
        evento.preventDefault();
        agregar(formTitulo, "titulos", {
            entidad: document.getElementById("tituloEntidad").value,
            nombreTitulo: document.getElementById("tituloNombre").value,
            descargable: document.getElementById("tituloDescargable").value
        }, document.getElementById("mensajeTitulo"), cargarTitulos);
    });

    const formExperiencia = document.getElementById("formExperiencia");
    formExperiencia.addEventListener("submit", evento => {
        evento.preventDefault();
        agregar(formExperiencia, "experiencias", {
            entidad: document.getElementById("experienciaEntidad").value,
            nombrePuesto: document.getElementById("experienciaPuesto").value,
            periodo: document.getElementById("experienciaPeriodo").value
        }, document.getElementById("mensajeExperiencia"), cargarExperiencias);
    });

    cargarTitulos();
    cargarExperiencias();
});
