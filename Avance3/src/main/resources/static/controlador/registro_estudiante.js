// Dirección del backend de Spring Boot
const API_REGISTRO = "http://localhost:8080/api/estudiantes/registro";

const formulario = document.getElementById("formRegistro");
const cajaMensaje = document.getElementById("mensaje");
const boton = document.getElementById("btnRegistrar");

function mostrarMensaje(texto, tipo, lista) {
    cajaMensaje.className = "alert alert-" + tipo + " mt-4 mb-0";
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
}

formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();

    const contrasena = document.getElementById("contrasena").value;
    const confirmar = document.getElementById("confirmarContrasena").value;

    if (!formulario.checkValidity()) {
        formulario.classList.add("was-validated");
        mostrarMensaje("Revisa los campos marcados en rojo.", "danger");
        return;
    }
    if (contrasena !== confirmar) {
        mostrarMensaje("Las contraseñas no coinciden.", "danger");
        return;
    }

    // Los nombres coinciden con los campos de RegistroEstudianteRequest
    const datos = {
        nombreEstudiante: document.getElementById("nombreEstudiante").value,
        apellidoPEstudiante: document.getElementById("apellidoPEstudiante").value,
        apellidoMEstudiante: document.getElementById("apellidoMEstudiante").value,
        dni: document.getElementById("dni").value,
        telefono: document.getElementById("telefono").value,
        correo: document.getElementById("correo").value,
        nivelEducativo: document.getElementById("nivelEducativo").value,
        descripcion: document.getElementById("descripcion").value,
        contrasena: contrasena,
        confirmarContrasena: confirmar
    };

    boton.disabled = true;
    try {
        const respuesta = await fetch(API_REGISTRO, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });
        const cuerpo = await respuesta.json().catch(() => ({}));

        if (respuesta.ok) {
            mostrarMensaje("Cuenta creada correctamente. Redirigiendo al inicio de sesión...", "success");
            setTimeout(() => { window.location.href = "index.html"; }, 1800);
        } else {
            mostrarMensaje(cuerpo.mensaje || "No se pudo crear la cuenta.", "danger",
                cuerpo.errores ? Object.values(cuerpo.errores) : null);
            boton.disabled = false;
        }
    } catch (error) {
        mostrarMensaje("No se pudo conectar con el servidor.", "danger");
        boton.disabled = false;
    }
});
