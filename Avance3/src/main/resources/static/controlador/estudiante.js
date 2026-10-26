// Dirección del backend de Spring Boot
const API_ESTUDIANTES = "http://localhost:8080/api/estudiantes/";

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

    try {
        const respuesta = await fetch(API_ESTUDIANTES + encodeURIComponent(id));

        if (respuesta.status === 404) {
            sessionStorage.removeItem("idEstudiante");
            window.location.href = "index.html";
            return;
        }
        if (!respuesta.ok) throw new Error("Error al obtener los datos");

        const e = await respuesta.json();

        // textContent (no innerHTML) para que lo escrito por el usuario no se interprete como HTML
        document.getElementById("nombreCompleto").textContent =
            e.apellidoPEstudiante + " " + e.apellidoMEstudiante + ", " + e.nombreEstudiante;
        document.getElementById("correo").textContent = e.correo;
        document.getElementById("telefono").textContent = e.telefono;
        document.getElementById("dni").textContent = e.dni;
        document.getElementById("nivelEducativo").textContent = e.nivelEducativo;
        document.getElementById("descripcion").textContent = e.descripcion;
    } catch (error) {
        document.getElementById("nombreCompleto").textContent = "No se pudieron cargar tus datos";
    }

    cargarTabla("titulos", "tablaTitulos", t => [t.entidad, t.nombreTitulo, t.descargable], 2, "No cuenta con títulos");
    cargarTabla("experiencias", "tablaExperiencias", x => [x.entidad, x.nombrePuesto, x.periodo], -1, "No cuenta con experiencia laboral");
});

// Solo se muestran enlaces http/https (evita enlaces tipo javascript:)
function esEnlaceSeguro(url) {
    return /^https?:\/\//i.test(url);
}

// Llena una tabla con los datos que devuelve la API.
// columnaEnlace: posición de la columna que es un enlace de descarga (-1 si no hay)
// textoVacio: mensaje que se muestra si no hay registros
async function cargarTabla(recurso, idTabla, obtenerFila, columnaEnlace, textoVacio) {
    const cuerpo = document.getElementById(idTabla);
    if (!cuerpo) return;
    const id = sessionStorage.getItem("idEstudiante");

    try {
        const respuesta = await fetch(API_ESTUDIANTES + encodeURIComponent(id) + "/" + recurso);
        if (!respuesta.ok) throw new Error();
        const items = await respuesta.json();

        cuerpo.replaceChildren();
        if (items.length === 0) {
            const tr = document.createElement("tr");
            const td = document.createElement("td");
            td.colSpan = 3;
            td.className = "text-muted";
            td.textContent = textoVacio;
            tr.appendChild(td);
            cuerpo.appendChild(tr);
            return;
        }

        items.forEach(item => {
            const tr = document.createElement("tr");
            obtenerFila(item).forEach((valor, posicion) => {
                const td = document.createElement("td");
                if (posicion === columnaEnlace) {
                    if (valor && esEnlaceSeguro(valor)) {
                        const a = document.createElement("a");
                        a.href = valor;
                        a.target = "_blank";
                        a.rel = "noopener noreferrer";
                        a.textContent = "Descargar";
                        td.appendChild(a);
                    } else {
                        td.textContent = "-";
                    }
                } else {
                    td.textContent = valor;   // textContent: lo escrito por el usuario no se interpreta como HTML
                }
                tr.appendChild(td);
            });
            cuerpo.appendChild(tr);
        });
    } catch (e) {
        cuerpo.innerHTML = "";
        const tr = document.createElement("tr");
        const td = document.createElement("td");
        td.colSpan = 3;
        td.className = "text-muted";
        td.textContent = "No se pudieron cargar los datos.";
        tr.appendChild(td);
        cuerpo.appendChild(tr);
    }
}
