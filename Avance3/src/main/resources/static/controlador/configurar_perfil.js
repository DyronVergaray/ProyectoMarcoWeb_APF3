// Dirección del backend de Spring Boot
const API_ESTUDIANTES = "http://localhost:8080/api/estudiantes/";

// Campos que se pueden modificar (coinciden con los id del HTML y con ActualizarEstudianteRequest)
const CAMPOS = ["nombreEstudiante", "apellidoPEstudiante", "apellidoMEstudiante",
    "dni", "correo", "telefono", "nivelEducativo", "descripcion"];

const cajaMensaje = document.getElementById("mensaje");
let datosGuardados = {};   // últimos datos confirmados por el servidor
let temporizador = null;

function mostrarMensaje(texto, tipo, lista) {
    clearTimeout(temporizador);
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
    if (tipo === "success") {
        temporizador = setTimeout(() => cajaMensaje.classList.add("d-none"), 3000);
    }
}

function mostrarDato(campo, valor) {
    const control = document.getElementById(campo);
    // Si el nivel educativo guardado no está en la lista, se agrega para poder mostrarlo
    if (control.tagName === "SELECT" && !Array.from(control.options).some(o => o.value === valor)) {
        control.add(new Option(valor, valor));
    }
    control.value = valor;
}

function bloquear(control, boton) {
    control.disabled = true;
    boton.textContent = "Cambiar";
    boton.classList.remove("btn-success");
    boton.classList.add("btn-outline-primary");
}

async function guardarCampo(control, boton) {
    const campo = control.id;

    // Se envían los datos ya guardados con solo este campo cambiado
    const datos = {};
    CAMPOS.forEach(c => datos[c] = datosGuardados[c]);
    datos[campo] = control.value;

    boton.disabled = true;
    try {
        const respuesta = await fetch(API_ESTUDIANTES + encodeURIComponent(sessionStorage.getItem("idEstudiante")), {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });
        const cuerpo = await respuesta.json().catch(() => ({}));

        if (respuesta.ok) {
            datosGuardados = cuerpo;
            mostrarDato(campo, cuerpo[campo]);
            bloquear(control, boton);
            mostrarMensaje("Cambio guardado correctamente.", "success");
        } else {
            // Se mantiene el campo en edición para que lo corrija
            mostrarMensaje(cuerpo.mensaje || "No se pudo guardar el cambio.", "danger",
                cuerpo.errores ? Object.values(cuerpo.errores) : null);
        }
    } catch (error) {
        mostrarMensaje("No se pudo conectar con el servidor.", "danger");
    }
    boton.disabled = false;
}

document.addEventListener("DOMContentLoaded", async () => {
    const id = sessionStorage.getItem("idEstudiante");

    // Si no hay sesión iniciada, volver al login
    if (!id) {
        window.location.href = "index.html";
        return;
    }

    document.getElementById("btnCerrarSesion").addEventListener("click", () => {
        sessionStorage.removeItem("idEstudiante");
        window.location.href = "index.html";
    });

    // Botones Cambiar / Guardar
    document.querySelectorAll(".btn-toggle").forEach(boton => {
        boton.addEventListener("click", () => {
            const control = boton.previousElementSibling;

            if (control.disabled) {
                // Habilitar edición
                control.disabled = false;
                control.focus();
                boton.textContent = "Guardar";
                boton.classList.remove("btn-outline-primary");
                boton.classList.add("btn-success");
            } else {
                guardarCampo(control, boton);
            }
        });
    });

    // Cargar los datos actuales del estudiante
    try {
        const respuesta = await fetch(API_ESTUDIANTES + encodeURIComponent(id));

        if (respuesta.status === 404) {
            sessionStorage.removeItem("idEstudiante");
            window.location.href = "index.html";
            return;
        }
        if (!respuesta.ok) throw new Error("Error al obtener los datos");

        datosGuardados = await respuesta.json();
        CAMPOS.forEach(campo => mostrarDato(campo, datosGuardados[campo]));
    } catch (error) {
        mostrarMensaje("No se pudieron cargar tus datos.", "danger");
    }
});
