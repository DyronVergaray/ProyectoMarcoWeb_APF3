const API_EMPRESAS = "http://localhost:8080/api/empresas/";
const API_USUARIOS = "http://localhost:8080/api/usuarios/";

const formulario = document.getElementById("formConfiguracion");
const cajaMensaje = document.getElementById("mensaje");
const botonGuardar = document.getElementById("btnGuardar");

// Los campos del formulario; sus ids coinciden con los de ModificarEmpresaRequest
const CAMPOS = ["nombreEmpresa", "rubro", "ruc", "telefono", "correoEmpresarial", "redesSociales"];

function mostrarMensaje(texto, tipo, lista) {
    cajaMensaje.className = "alert alert-" + tipo;
    cajaMensaje.textContent = texto;

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

function bloquear(boton) {
    boton.previousElementSibling.disabled = true;
    boton.textContent = "Cambiar";
    boton.classList.remove("btn-success");
    boton.classList.add("btn-outline-primary");
}

function desbloquear(boton) {
    const campo = boton.previousElementSibling;
    campo.disabled = false;
    campo.focus();
    boton.textContent = "Listo";
    boton.classList.remove("btn-outline-primary");
    boton.classList.add("btn-success");
}

function bloquearTodos() {
    document.querySelectorAll(".btn-toggle").forEach(bloquear);
}

function llenar(empresa) {
    CAMPOS.forEach(campo => {
        document.getElementById(campo).value = empresa[campo] || "";
    });
}

async function guardar(evento) {
    evento.preventDefault();

    const datos = {};
    CAMPOS.forEach(campo => {
        datos[campo] = document.getElementById(campo).value;
    });

    botonGuardar.disabled = true;
    try {
        const respuesta = await fetch(API_USUARIOS + encodeURIComponent(sessionStorage.getItem("idUsuario")) + "/empresa", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });
        const cuerpo = await respuesta.json().catch(() => ({}));

        if (respuesta.ok) {
            llenar(cuerpo);
            bloquearTodos();
            mostrarMensaje("Los datos de la empresa se guardaron correctamente.", "success");
        } else {
            mostrarMensaje(cuerpo.mensaje || "No se pudieron guardar los cambios.", "danger",
                cuerpo.errores ? Object.values(cuerpo.errores) : null);
        }
    } catch (e) {
        mostrarMensaje("No se pudo conectar con el servidor.", "danger");
    }
    botonGuardar.disabled = false;
}

document.addEventListener("DOMContentLoaded", async () => {
    const idUsuario = sessionStorage.getItem("idUsuario");
    const idEmpresa = sessionStorage.getItem("idEmpresa");

    if (!idUsuario || !idEmpresa) {
        window.location.href = "index.html";
        return;
    }
    if ((sessionStorage.getItem("rolUsuario") || "").toLowerCase() !== "admin") {
        window.location.href = "perfil.html";
        return;
    }

    document.querySelectorAll(".btn-toggle").forEach(boton => {
        boton.addEventListener("click", () => {
            if (boton.previousElementSibling.disabled) {
                desbloquear(boton);
            } else {
                bloquear(boton);
            }
        });
    });
    formulario.addEventListener("submit", guardar);

    try {
        const respuesta = await fetch(API_EMPRESAS + encodeURIComponent(idEmpresa));
        if (!respuesta.ok) throw new Error("Error al obtener los datos");
        llenar(await respuesta.json());
    } catch (e) {
        botonGuardar.disabled = true;
        mostrarMensaje("No se pudieron cargar los datos de la empresa. Intenta recargar la página.", "danger");
    }
});
