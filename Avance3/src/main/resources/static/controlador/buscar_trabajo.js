// Dirección del backend de Spring Boot
const API_OFERTAS = "http://localhost:8080/api/ofertas";
const API_ESTUDIANTES = "http://localhost:8080/api/estudiantes/";

let ofertas = [];                 // todas las ofertas traídas del backend
let idsPostuladas = new Set();    // ids de las ofertas a las que este estudiante ya postuló
let idEstudiante = null;

const listaOfertas = document.getElementById("listaOfertas");
const contador = document.getElementById("contador");
const cajaMensaje = document.getElementById("mensaje");

// Controles de filtro
const buscarPuesto = document.getElementById("buscarPuesto");
const filtroEmpresa = document.getElementById("filtroEmpresa");
const filtroEstado = document.getElementById("filtroEstado");
const filtroFrecuencia = document.getElementById("filtroFrecuencia");
const filtroSueldoMin = document.getElementById("filtroSueldoMin");
const filtroSueldoMax = document.getElementById("filtroSueldoMax");
const filtroHoras = document.getElementById("filtroHoras");
const filtroSeguro = document.getElementById("filtroSeguro");
const filtroBonos = document.getElementById("filtroBonos");

// ---------- Utilidades ----------

// Minúsculas y sin tildes, para que "ingenieria" encuentre "Ingeniería"
function normalizar(texto) {
    return (texto || "").normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase().trim();
}

function formatoSoles(numero) {
    return Number(numero).toLocaleString("es-PE", { style: "currency", currency: "PEN" });
}

// "2026-10-25" -> "25/10/2026" (sin pasar por Date para evitar desfases de zona horaria)
function formatoFecha(iso) {
    const [anio, mes, dia] = iso.split("-");
    return dia + "/" + mes + "/" + anio;
}

function hoy() {
    const d = new Date();
    return d.getFullYear() + "-" + String(d.getMonth() + 1).padStart(2, "0") + "-" + String(d.getDate()).padStart(2, "0");
}

function iniciales(nombre) {
    const palabras = nombre.trim().split(/\s+/);
    return (palabras[0].charAt(0) + (palabras[1] ? palabras[1].charAt(0) : "")).toUpperCase();
}

function crear(etiqueta, clase, texto) {
    const el = document.createElement(etiqueta);
    if (clase) el.className = clase;
    if (texto !== undefined) el.textContent = texto; // textContent: lo escrito por la empresa nunca se interpreta como HTML
    return el;
}

// Estado de vigencia según las fechas de la oferta
function vigencia(oferta) {
    const fecha = hoy();
    if (oferta.fechaFin < fecha) return { texto: "Plazo vencido el " + formatoFecha(oferta.fechaFin), clase: "text-danger" };
    if (oferta.fechaInicio > fecha) return { texto: "Inicia el " + formatoFecha(oferta.fechaInicio), clase: "text-secondary" };
    return { texto: "Vigente hasta el " + formatoFecha(oferta.fechaFin), clase: "text-success" };
}

function mostrarMensaje(texto, tipo) {
    cajaMensaje.className = "alert alert-" + tipo;
    cajaMensaje.textContent = texto;
    cajaMensaje.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

// Estado del botón: solo se puede postular si la oferta está "Activa" y aún no se postuló.
// El estado de la oferta lo cambia únicamente el administrador de la empresa.
function estadoPostulacion(oferta) {
    if (idsPostuladas.has(oferta.id)) return { texto: "Postulado", habilitado: false };
    if (oferta.estado !== "Activa") return { texto: oferta.estado, habilitado: false };
    return { texto: "Postular", habilitado: true };
}

// Postula al estudiante a la oferta (el backend asigna el estado "Activa" y la fecha de hoy)
async function postular(oferta, boton) {
    boton.disabled = true;
    try {
        const respuesta = await fetch(API_ESTUDIANTES + encodeURIComponent(idEstudiante) + "/postulaciones", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ idOferta: oferta.id })
        });
        const cuerpo = await respuesta.json().catch(() => ({}));

        if (respuesta.ok) {
            idsPostuladas.add(oferta.id);
            mostrarMensaje("Te postulaste a \"" + oferta.puesto + "\" en " + oferta.nombreEmpresa + ".", "success");
        } else {
            // 409: ya postulaste o la oferta finalizó
            if (respuesta.status === 409 && cuerpo.mensaje === "Ya postulaste a esta oferta.") idsPostuladas.add(oferta.id);
            mostrarMensaje(cuerpo.mensaje || "No se pudo completar la postulación.", "danger");
        }
    } catch (e) {
        mostrarMensaje("No se pudo conectar con el servidor.", "danger");
    }
    filtrar(); // vuelve a pintar las tarjetas con el botón actualizado
    actualizarBotonModal(oferta);
}

// ---------- Pintar ofertas ----------

function crearTarjeta(oferta) {
    const tarjeta = crear("article", "oferta");
    tarjeta.appendChild(crear("div", "oferta-avatar", iniciales(oferta.nombreEmpresa)));

    const cuerpo = crear("div", "oferta-cuerpo");
    cuerpo.appendChild(crear("h2", "oferta-puesto", oferta.puesto));
    const lineaEmpresa = crear("div", "d-flex flex-wrap align-items-center gap-2 mb-2");
    lineaEmpresa.appendChild(crear("span", "oferta-empresa", oferta.nombreEmpresa));
    lineaEmpresa.appendChild(crear("span", "estado-oferta estado-" + oferta.estado.toLowerCase().replace(/\s+/g, "-"), oferta.estado));
    cuerpo.appendChild(lineaEmpresa);

    const chips = crear("div", "d-flex flex-wrap gap-2 mb-2");
    chips.appendChild(crear("span", "chip chip-sueldo", formatoSoles(oferta.sueldo) + " · " + oferta.frecuenciaPagos));
    chips.appendChild(crear("span", "chip", oferta.horas + " h/semana"));
    if (Number(oferta.bonos) > 0) chips.appendChild(crear("span", "chip", "Bonos " + formatoSoles(oferta.bonos)));
    if (oferta.seguroVida === "Sí") chips.appendChild(crear("span", "chip", "Seguro de vida"));
    if (oferta.vacaciones > 0) chips.appendChild(crear("span", "chip", oferta.vacaciones + " días de vacaciones"));
    cuerpo.appendChild(chips);

    const v = vigencia(oferta);
    cuerpo.appendChild(crear("div", "oferta-fecha " + v.clase, v.texto));

    const botones = crear("div", "oferta-botones");

    const detalle = crear("button", "btn btn-outline-secondary text-nowrap", "Ver detalles");
    detalle.type = "button";
    detalle.addEventListener("click", () => abrirDetalle(oferta));

    const estado = estadoPostulacion(oferta);
    const postularBtn = crear("button", "btn text-nowrap " + (estado.habilitado ? "btn-primario" : "btn-outline-success"), estado.texto);
    postularBtn.type = "button";
    postularBtn.disabled = !estado.habilitado;
    postularBtn.addEventListener("click", () => postular(oferta, postularBtn));

    botones.append(postularBtn, detalle);
    tarjeta.append(cuerpo, botones);
    return tarjeta;
}

function pintar(lista) {
    listaOfertas.textContent = "";
    contador.textContent = "Mostrando " + lista.length + " de " + ofertas.length;

    if (lista.length === 0) {
        const vacio = crear("div", "tarjeta text-center text-secondary",
            ofertas.length === 0 ? "Todavía no hay ofertas publicadas." : "Ninguna oferta coincide con los filtros.");
        listaOfertas.appendChild(vacio);
        return;
    }
    lista.forEach(o => listaOfertas.appendChild(crearTarjeta(o)));
}

// ---------- Detalle ----------

// Deja el botón del modal igual que el de la tarjeta de esa oferta
function actualizarBotonModal(oferta) {
    const boton = document.getElementById("btnPostularModal");
    if (boton.dataset.idOferta !== String(oferta.id)) return;
    const estado = estadoPostulacion(oferta);
    boton.textContent = estado.texto;
    boton.disabled = !estado.habilitado;
}

function abrirDetalle(o) {
    document.getElementById("modalOfertaTitulo").textContent = o.puesto;
    document.getElementById("modalOfertaEmpresa").textContent = o.nombreEmpresa;

    const filas = [
        ["Sueldo", formatoSoles(o.sueldo) + " (" + o.frecuenciaPagos.toLowerCase() + ")"],
        ["Horas", o.horas + " horas semanales"],
        ["Bonos", Number(o.bonos) > 0 ? formatoSoles(o.bonos) : "No incluye"],
        ["Gratificaciones", o.gratificaciones || "No especifica"],
        ["Seguro de vida", o.seguroVida],
        ["Vacaciones", o.vacaciones + " días"],
        ["Otros beneficios", o.otros || "No especifica"],
        ["Vigencia", formatoFecha(o.fechaInicio) + " al " + formatoFecha(o.fechaFin)],
        ["Estado", o.estado]
    ];

    const cuerpo = document.getElementById("modalOfertaCuerpo");
    cuerpo.textContent = "";

    const dl = crear("dl", "row mb-0");
    filas.forEach(([etiqueta, valor]) => {
        dl.appendChild(crear("dt", "col-sm-4", etiqueta));
        dl.appendChild(crear("dd", "col-sm-8", valor));
    });
    cuerpo.appendChild(dl);

    cuerpo.appendChild(crear("h3", "fs-6 fw-bold mt-3", "Requisitos"));
    if (o.requisitos.length === 0) {
        cuerpo.appendChild(crear("p", "text-secondary mb-0", "Esta oferta no pide requisitos específicos."));
    } else {
        const ul = crear("ul", "mb-0");
        o.requisitos.forEach(r => ul.appendChild(crear("li", "", r.nombreRequisito)));
        cuerpo.appendChild(ul);
    }

    const botonModal = document.getElementById("btnPostularModal");
    botonModal.dataset.idOferta = o.id;
    botonModal.onclick = () => postular(o, botonModal);
    actualizarBotonModal(o);

    bootstrap.Modal.getOrCreateInstance(document.getElementById("modalOferta")).show();
}

// ---------- Filtros ----------

function filtrar() {
    const texto = normalizar(buscarPuesto.value);
    const min = filtroSueldoMin.value === "" ? null : Number(filtroSueldoMin.value);
    const max = filtroSueldoMax.value === "" ? null : Number(filtroSueldoMax.value);
    const rangoHoras = filtroHoras.value ? filtroHoras.value.split("-").map(Number) : null;

    const resultado = ofertas.filter(o => {
        // Texto: el puesto debe incluir lo escrito (como un includes)
        if (texto && !normalizar(o.puesto).includes(texto)) return false;
        if (filtroEmpresa.value && String(o.idEmpresa) !== filtroEmpresa.value) return false;
        if (filtroEstado.value && o.estado !== filtroEstado.value) return false;
        if (filtroFrecuencia.value && o.frecuenciaPagos !== filtroFrecuencia.value) return false;
        if (min !== null && Number(o.sueldo) < min) return false;
        if (max !== null && Number(o.sueldo) > max) return false;
        if (rangoHoras && (o.horas < rangoHoras[0] || o.horas > rangoHoras[1])) return false;
        if (filtroSeguro.value && o.seguroVida !== filtroSeguro.value) return false;
        if (filtroBonos.checked && !(Number(o.bonos) > 0)) return false;
        return true;
    });
    pintar(resultado);
}

// Las empresas del filtro salen de las ofertas que hay (sin repetir, en orden alfabético)
function llenarEmpresas() {
    const empresas = new Map();
    ofertas.forEach(o => empresas.set(o.idEmpresa, o.nombreEmpresa));

    Array.from(empresas.entries())
        .sort((a, b) => a[1].localeCompare(b[1], "es"))
        .forEach(([id, nombre]) => {
            const opcion = document.createElement("option");
            opcion.value = id;
            opcion.textContent = nombre;
            filtroEmpresa.appendChild(opcion);
        });
}

function limpiarFiltros() {
    buscarPuesto.value = "";
    filtroEmpresa.value = "";
    filtroEstado.value = "";
    filtroFrecuencia.value = "";
    filtroSueldoMin.value = "";
    filtroSueldoMax.value = "";
    filtroHoras.value = "";
    filtroSeguro.value = "";
    filtroBonos.checked = false;
    filtrar();
}

// ---------- Inicio ----------

document.addEventListener("DOMContentLoaded", async () => {
    // Si no hay sesión de estudiante iniciada, volver al login
    idEstudiante = sessionStorage.getItem("idEstudiante");
    if (!idEstudiante) {
        window.location.href = "index.html";
        return;
    }

    // Cerrar sesión: se borra el id guardado y se vuelve al login
    document.getElementById("btnCerrarSesion").addEventListener("click", () => {
        sessionStorage.removeItem("idEstudiante");
        window.location.href = "index.html";
    });

    // Filtrado en vivo: al escribir o cambiar cualquier filtro
    [buscarPuesto, filtroSueldoMin, filtroSueldoMax].forEach(c => c.addEventListener("input", filtrar));
    [filtroEmpresa, filtroEstado, filtroFrecuencia, filtroHoras, filtroSeguro, filtroBonos].forEach(c => c.addEventListener("change", filtrar));
    document.getElementById("formBuscar").addEventListener("submit", (e) => { e.preventDefault(); filtrar(); });
    document.getElementById("btnLimpiar").addEventListener("click", limpiarFiltros);

    try {
        const respuesta = await fetch(API_OFERTAS);
        if (!respuesta.ok) throw new Error("Error al obtener las ofertas");
        ofertas = await respuesta.json();
    } catch (e) {
        listaOfertas.textContent = "";
        contador.textContent = "";
        mostrarMensaje("No se pudieron cargar las ofertas. Verifica que el servidor esté en marcha e intenta de nuevo.", "danger");
        return;
    }

    // Ofertas a las que ya postuló (para mostrar "Postulado"); si falla, el servidor igual evita duplicados
    try {
        const respuesta = await fetch(API_ESTUDIANTES + encodeURIComponent(idEstudiante) + "/postulaciones");
        if (respuesta.ok) {
            const postulaciones = await respuesta.json();
            idsPostuladas = new Set(postulaciones.map(p => p.idOferta));
        }
    } catch (e) {
        console.error(e);
    }

    llenarEmpresas();
    filtrar();
});
