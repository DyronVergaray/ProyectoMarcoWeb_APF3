// Dirección del backend de Spring Boot
const API_REGISTRO_EMPRESA = "http://localhost:8080/api/empresas/registro";

const formulario = document.getElementById("formRegistroEmpresa");
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

    // Los nombres coinciden con los campos de RegistroEmpresaRequest
    const datos = {
        nombreEmpresa: document.getElementById("nombreEmpresa").value,
        rubro: document.getElementById("rubro").value,
        ruc: document.getElementById("ruc").value,
        telefono: document.getElementById("telefono").value,
        correoEmpresarial: document.getElementById("correoEmpresarial").value,
        redesSociales: document.getElementById("redesSociales").value,
        nombreUsuario: document.getElementById("nombreUsuario").value,
        apellidoPUsuario: document.getElementById("apellidoPUsuario").value,
        apellidoMUsuario: document.getElementById("apellidoMUsuario").value,
        dni: document.getElementById("dni").value,
        correo: document.getElementById("correo").value,
        contrasena: contrasena,
        confirmarContrasena: confirmar
    };

    boton.disabled = true;
    try {
        const respuesta = await fetch(API_REGISTRO_EMPRESA, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });
        const cuerpo = await respuesta.json().catch(() => ({}));

        if (respuesta.ok) {
            mostrarMensaje("Empresa creada correctamente. Redirigiendo al inicio de sesión...", "success");
            setTimeout(() => { window.location.href = "index.html"; }, 1800);
        } else {
            mostrarMensaje(cuerpo.mensaje || "No se pudo crear la empresa.", "danger",
                cuerpo.errores ? Object.values(cuerpo.errores) : null);
            boton.disabled = false;
        }
    } catch (error) {
        mostrarMensaje("No se pudo conectar con el servidor.", "danger");
        boton.disabled = false;
    }
});
