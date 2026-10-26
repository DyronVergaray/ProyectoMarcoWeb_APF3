// Direcciones del backend de Spring Boot
const API_USUARIOS = "http://localhost:8080/api/usuarios/";
const API_REQUISITOS = "http://localhost:8080/api/requisitos-oferta";

let idUsuario = null;
let ofertas = [];            // ofertas de la empresa
let requisitos = [];         // requisitos disponibles
let ofertaEditando = null;   // oferta que se está modificando en el modal

const tabla = document.getElementById("tablaVacantes");
const cajaMensaje = document.getElementById("mensaje");
const cajaMensajeModal = document.getElementById("mensajeModal");
const formulario = document.getElementById("formModificar");
const listaRequisitos = document.getElementById("listaRequisitos");
const botonGuardar = document.getElementById("btnGuardar");
const modal = () => bootstrap.Modal.getOrCreateInstance(document.getElementById("modalModificar"));

// ---------- Utilidades ----------

function formatoSoles(numero) {
    return Number(numero).toLocaleString("es-PE", { style: "currency", currency: "PEN" });
}

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

function mostrar(caja, texto, tipo, lista) {
    caja.className = "alert alert-" + tipo;
    caja.textContent = texto;
    if (lista && lista.length > 0) {
        const ul = document.createElement("ul");
        ul.className = "mb-0 mt-2";
        lista.forEach(m => ul.appendChild(crear("li", "", m)));
        caja.appendChild(ul);
    }
    caja.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

const CLASE_ESTADO = {
    "Activa": "bg-success",
    "En Proceso": "bg-warning text-dark",
    "Finalizada": "bg-secondary"
};

// ---------- Tabla ----------

function pintarTabla() {
    tabla.textContent = "";

    if (ofertas.length === 0) {
        const fila = crear("tr");
        const celda = crear("td", "text-center text-secondary py-4");
        celda.colSpan = 7;
        celda.append("Todavía no has creado ninguna oferta. ");
        const enlace = crear("a", "", "Crear oferta");
        enlace.href = "crear_oferta.html";
        celda.appendChild(enlace);
        fila.appendChild(celda);
        tabla.appendChild(fila);
        return;
    }

    ofertas.forEach(o => {
        const fila = crear("tr");
        fila.appendChild(crear("td", "fw-medium", o.puesto));
        fila.appendChild(crear("td", "", formatoSoles(o.sueldo) + " · " + o.frecuenciaPagos));
        fila.appendChild(crear("td", "", o.horas));
        fila.appendChild(crear("td", "", formatoFecha(o.fechaInicio)));
        fila.appendChild(crear("td", "", formatoFecha(o.fechaFin)));

        const celdaEstado = crear("td");
        celdaEstado.appendChild(crear("span", "badge " + (CLASE_ESTADO[o.estado] || "bg-secondary"), o.estado));
        fila.appendChild(celdaEstado);

        // Acciones: ver quiénes postularon a la oferta o modificarla
        const celdaAcciones = crear("td", "text-end text-nowrap");

        const verPostulantes = crear("a", "btn btn-sm btn-outline-secondary me-2", "Ver postulantes");
        verPostulantes.href = "postulantes.html?idOferta=" + encodeURIComponent(o.id);

        const boton = crear("button", "btn btn-sm btn-outline-primary", "Modificar");
        boton.type = "button";
        boton.addEventListener("click", () => abrirModal(o));

        celdaAcciones.append(verPostulantes, boton);
        fila.appendChild(celdaAcciones);

        tabla.appendChild(fila);
    });
}

// ---------- Requisitos ----------

function actualizarContador() {
    document.getElementById("totalSeleccionados").textContent =
        listaRequisitos.querySelectorAll("input[type=checkbox]:checked").length;
}

function pintarRequisitos() {
    listaRequisitos.textContent = "";
    if (requisitos.length === 0) {
        listaRequisitos.textContent = "No hay requisitos disponibles.";
        return;
    }
    requisitos.forEach(r => {
        const contenedor = crear("div", "form-check");

        const check = crear("input", "form-check-input");
        check.type = "checkbox";
        check.id = "requisito-" + r.id;
        check.value = r.id;
        check.addEventListener("change", actualizarContador);

        const etiqueta = crear("label", "form-check-label", r.nombreRequisito);
        etiqueta.htmlFor = check.id;

        contenedor.append(check, etiqueta);
        listaRequisitos.appendChild(contenedor);
    });
}

// ---------- Modal de modificación ----------

function valor(id) { return document.getElementById(id); }

function abrirModal(o) {
    ofertaEditando = o;
    formulario.classList.remove("was-validated");
    cajaMensajeModal.className = "alert d-none";

    valor("puesto").value = o.puesto;
    valor("estado").value = o.estado;
    valor("sueldo").value = o.sueldo;
    valor("frecuenciaPagos").value = o.frecuenciaPagos;
    valor("bonos").value = o.bonos;
    valor("horas").value = o.horas;
    valor("vacaciones").value = o.vacaciones;
    valor("seguroVida").value = o.seguroVida;
    valor("gratificaciones").value = o.gratificaciones;
    valor("otros").value = o.otros;
    valor("fechaInicio").value = o.fechaInicio;
    valor("fechaFin").value = o.fechaFin;
    valor("fechaFin").min = o.fechaInicio;

    const idsMarcados = new Set(o.requisitos.map(r => r.id));
    listaRequisitos.querySelectorAll("input[type=checkbox]").forEach(c => {
        c.checked = idsMarcados.has(Number(c.value));
    });
    actualizarContador();

    modal().show();
}

async function guardar(evento) {
    evento.preventDefault();

    if (!formulario.checkValidity()) {
        formulario.classList.add("was-validated");
        mostrar(cajaMensajeModal, "Revisa los campos marcados en rojo.", "danger");
        return;
    }

    // Los nombres coinciden con los campos de ModificarOfertaRequest
    const datos = {
        puesto: valor("puesto").value,
        sueldo: Number(valor("sueldo").value),
        horas: parseInt(valor("horas").value, 10),
        frecuenciaPagos: valor("frecuenciaPagos").value,
        bonos: Number(valor("bonos").value || 0),
        gratificaciones: valor("gratificaciones").value,
        seguroVida: valor("seguroVida").value,
        vacaciones: parseInt(valor("vacaciones").value, 10),
        otros: valor("otros").value,
        estado: valor("estado").value,
        fechaInicio: valor("fechaInicio").value,
        fechaFin: valor("fechaFin").value,
        idsRequisitos: Array.from(listaRequisitos.querySelectorAll("input[type=checkbox]:checked"))
            .map(c => Number(c.value))
    };

    botonGuardar.disabled = true;
    try {
        const url = API_USUARIOS + encodeURIComponent(idUsuario) + "/ofertas/" + encodeURIComponent(ofertaEditando.id);
        const respuesta = await fetch(url, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });
        const cuerpo = await respuesta.json().catch(() => ({}));

        if (respuesta.ok) {
            // Reemplaza la oferta en la lista local y vuelve a pintar la tabla
            ofertas = ofertas.map(o => o.id === cuerpo.id ? cuerpo : o);
            pintarTabla();
            modal().hide();
            mostrar(cajaMensaje, "Oferta \"" + cuerpo.puesto + "\" actualizada correctamente.", "success");
        } else {
            mostrar(cajaMensajeModal, cuerpo.mensaje || "No se pudo modificar la oferta.", "danger",
                cuerpo.errores ? Object.values(cuerpo.errores) : null);
        }
    } catch (e) {
        mostrar(cajaMensajeModal, "No se pudo conectar con el servidor.", "danger");
    }
    botonGuardar.disabled = false;
}

// ---------- Inicio ----------

document.addEventListener("DOMContentLoaded", async () => {
    idUsuario = sessionStorage.getItem("idUsuario");

    // Si no hay sesión iniciada, volver al login
    if (!idUsuario) {
        window.location.href = "index.html";
        return;
    }
    // Solo el administrador gestiona las vacantes (el backend también lo verifica)
    if ((sessionStorage.getItem("rolUsuario") || "").toLowerCase() !== "admin") {
        window.location.href = "perfil.html";
        return;
    }

    valor("fechaInicio").addEventListener("change", () => {
        valor("fechaFin").min = valor("fechaInicio").value;
    });
    formulario.addEventListener("submit", guardar);

    try {
        const [respOfertas, respRequisitos] = await Promise.all([
            fetch(API_USUARIOS + encodeURIComponent(idUsuario) + "/ofertas"),
            fetch(API_REQUISITOS)
        ]);
        if (!respOfertas.ok || !respRequisitos.ok) throw new Error("Error al obtener los datos");

        ofertas = await respOfertas.json();
        requisitos = await respRequisitos.json();
        pintarRequisitos();
        pintarTabla();
    } catch (e) {
        tabla.textContent = "";
        mostrar(cajaMensaje, "No se pudieron cargar las vacantes. Verifica que el servidor esté en marcha e intenta de nuevo.", "danger");
    }
});
