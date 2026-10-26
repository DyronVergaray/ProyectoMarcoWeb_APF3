// Dirección del backend de Spring Boot
const API_LOGIN_ESTUDIANTE = "http://localhost:8080/api/estudiantes/login";
const API_LOGIN_USUARIO = "http://localhost:8080/api/usuarios/login";

// Envía el correo y la contraseña a un endpoint de login
function enviarLogin(url, correo, contrasena) {
    return fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ correo: correo, contrasena: contrasena })
    });
}

async function inicio_sesion() {
    const correo = document.getElementById("exampleInputEmail1").value;
    const contrasena = document.getElementById("exampleInputPassword1").value;
    const error = document.getElementById("error");

    if (correo.trim() === "" || contrasena === "") {
        error.textContent = "Ingresa tu correo y contraseña";
        return;
    }

    try {
        // 1) Intentar como estudiante
        const respEstudiante = await enviarLogin(API_LOGIN_ESTUDIANTE, correo, contrasena);
        if (respEstudiante.ok) {
            const estudiante = await respEstudiante.json();
            sessionStorage.clear();
            // Guardamos el id para que estudiante.html sepa quién inició sesión
            sessionStorage.setItem("idEstudiante", estudiante.id);
            window.location.href = "estudiante.html";
            return;
        }

        // 2) Si no es estudiante, intentar como usuario de una empresa
        const respUsuario = await enviarLogin(API_LOGIN_USUARIO, correo, contrasena);
        if (respUsuario.ok) {
            const usuario = await respUsuario.json();
            sessionStorage.clear();
            // perfil.html usa idEmpresa; el navbar muestra el nombre
            sessionStorage.setItem("idUsuario", usuario.id);
            sessionStorage.setItem("idEmpresa", usuario.idEmpresa);
            sessionStorage.setItem("rolUsuario", usuario.rol);
            sessionStorage.setItem("nombreUsuario", usuario.nombreUsuario + " " + usuario.apellidoPUsuario);
            window.location.href = "perfil.html";
            return;
        }

        // 3) Ninguno aceptó los datos
        const cuerpo = await respUsuario.json().catch(() => ({}));
        error.textContent = cuerpo.mensaje || "Correo o contraseña incorrectos.";
    } catch (e) {
        error.textContent = "No se pudo conectar con el servidor.";
    }
}
