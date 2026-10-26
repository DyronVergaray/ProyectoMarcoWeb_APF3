// Direcciones del backend de Spring Boot
const API_USUARIOS = "http://localhost:8080/api/usuarios/";
const API_REQUISITOS = "http://localhost:8080/api/requisitos-oferta";

const formulario = document.getElementById("formOferta");
const cajaMensaje = document.getElementById("mensaje");
const boton = document.getElementById("btnCrearOferta");
const listaRequisitos = document.getElementById("listaRequisitos");

function mostrarMensaje(texto, tipo, lista) {
    cajaMensaje.className = "alert alert-" + tipo;
    cajaMensaje.textContent = texto;

    // Lista de errores de validación enviados por el backend
    if (lista && lista.length > 0) {
        const ul = document.createElement("ul");
        ul.className = "mb-0 mt-2";
        lista.forEach(mensaje => {
            const li = document.createElement("li");
            li.textContent = mensaje;
            ul.appendChild(li);
        });
        cajaMensaje.appendChild(ul);
    }
    cajaMensaje.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

// Fecha de hoy (hora local) en formato yyyy-MM-dd, como la usa <input type="date">
function hoy() {
    const d = new Date();
    const mes = String(d.getMonth() + 1).padStart(2, "0");
    const dia = String(d.getDate()).padStart(2, "0");
    return d.getFullYear() + "-" + mes + "-" + dia;
}

function actualizarContador() {
    const marcados = listaRequisitos.querySelectorAll("input[type=checkbox]:checked").length;
    document.getElementById("totalSeleccionados").textContent = marcados;
}

// Dibuja un checkbox por cada requisito guardado en la base de datos
async function cargarRequisitos() {
    try {
        const respuesta = await fetch(API_REQUISITOS);
        if (!respuesta.ok) throw new Error("Error al obtener los requisitos");
        const requisitos = await respuesta.json();

        listaRequisitos.textContent = "";
        if (requisitos.length === 0) {
            listaRequisitos.textContent = "No hay requisitos disponibles.";
            return;
        }

        requisitos.forEach(r => {
            const contenedor = document.createElement("div");
            contenedor.className = "form-check";

            const check = document.createElement("input");
            check.type = "checkbox";
            check.className = "form-check-input";
            check.id = "requisito-" + r.id;
            check.value = r.id;
            check.addEventListener("change", actualizarContador);

            const etiqueta = document.createElement("label");
            etiqueta.className = "form-check-label";
            etiqueta.htmlFor = check.id;
            etiqueta.textContent = r.nombreRequisito;

            contenedor.append(check, etiqueta);
            listaRequisitos.appendChild(contenedor);
        });
    } catch (e) {
        listaRequisitos.textContent = "No se pudieron cargar los requisitos.";
        mostrarMensaje("No se pudieron cargar los requisitos. Intenta recargar la página.", "danger");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const idUsuario = sessionStorage.getItem("idUsuario");

    // Si no hay sesión iniciada, volver al login
    if (!idUsuario) {
        window.location.href = "index.html";
        return;
    }
    // Solo el administrador puede crear ofertas (el backend también lo verifica)
    if ((sessionStorage.getItem("rolUsuario") || "").toLowerCase() !== "admin") {
        window.location.href = "perfil.html";
        return;
    }

    // La oferta no puede empezar antes de hoy ni terminar antes de empezar
    const fechaInicio = document.getElementById("fechaInicio");
    const fechaFin = document.getElementById("fechaFin");
    fechaInicio.min = hoy();
    fechaFin.min = hoy();
    fechaInicio.addEventListener("change", () => {
        fechaFin.min = fechaInicio.value || hoy();
    });

    cargarRequisitos();

    formulario.addEventListener("submit", async (evento) => {
        evento.preventDefault();

        if (!formulario.checkValidity()) {
            formulario.classList.add("was-validated");
            mostrarMensaje("Revisa los campos marcados en rojo.", "danger");
            return;
        }

        // Los nombres coinciden con los campos de CrearOfertaRequest
        const datos = {
            puesto: document.getElementById("puesto").value,
            sueldo: Number(document.getElementById("sueldo").value),
            horas: parseInt(document.getElementById("horas").value, 10),
            frecuenciaPagos: document.getElementById("frecuenciaPagos").value,
            bonos: Number(document.getElementById("bonos").value || 0),
            gratificaciones: document.getElementById("gratificaciones").value,
            seguroVida: document.getElementById("seguroVida").value,
            vacaciones: parseInt(document.getElementById("vacaciones").value, 10),
            otros: document.getElementById("otros").value,
            fechaInicio: fechaInicio.value,
            fechaFin: fechaFin.value,
            idsRequisitos: Array.from(listaRequisitos.querySelectorAll("input[type=checkbox]:checked"))
                .map(c => Number(c.value))
        };

        boton.disabled = true;
        try {
            const respuesta = await fetch(API_USUARIOS + encodeURIComponent(idUsuario) + "/ofertas", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(datos)
            });
            const cuerpo = await respuesta.json().catch(() => ({}));

            if (respuesta.ok) {
                mostrarMensaje("Oferta \"" + cuerpo.puesto + "\" creada correctamente.", "success");
                formulario.reset();
                formulario.classList.remove("was-validated");
                actualizarContador();
            } else {
                mostrarMensaje(cuerpo.mensaje || "No se pudo crear la oferta.", "danger",
                    cuerpo.errores ? Object.values(cuerpo.errores) : null);
            }
        } catch (e) {
            mostrarMensaje("No se pudo conectar con el servidor.", "danger");
        }
        boton.disabled = false;
    });
});
