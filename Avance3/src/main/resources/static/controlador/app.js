// Carga los componentes compartidos (navbar.html y sidebar.html) dentro de cada página
document.addEventListener("DOMContentLoaded", () => {

    // Carga un componente HTML (navbar / sidebar) dentro de un contenedor
    const cargarComponente = (idContenedor, rutaArchivo, alCargar) => {
        fetch(rutaArchivo)
            .then(response => {
                if (!response.ok) throw new Error("Error al cargar " + rutaArchivo);
                return response.text();
            })
            .then(html => {
                document.getElementById(idContenedor).innerHTML = html;
                if (alCargar) alCargar();
            })
            .catch(error => console.error(error));
    };

    cargarComponente("navbar-contenedor", "navbar.html", () => {
        // Muestra en el navbar el nombre del usuario que inició sesión
        const nombre = sessionStorage.getItem("nombreUsuario");
        const etiqueta = document.getElementById("navbar-nombre");
        if (nombre && etiqueta) etiqueta.textContent = nombre;
    });

    cargarComponente("sidebar-contenedor", "sidebar.html", () => {
        // Solo el administrador ve el enlace para crear ofertas
        const rol = (sessionStorage.getItem("rolUsuario") || "").toLowerCase();
        const itemCrearOferta = document.getElementById("itemCrearOferta");
        if (itemCrearOferta && rol !== "admin") itemCrearOferta.classList.add("d-none");

        const btnCerrarSesion = document.getElementById("btnCerrarSesion");
        if (btnCerrarSesion) {
            btnCerrarSesion.addEventListener("click", (evento) => {
                const confirmar = confirm("¿Seguro que deseas cerrar sesión?");
                if (!confirmar) {
                    evento.preventDefault();
                    return;
                }
                // Al confirmar se borra la sesión y el enlace lleva a index.html
                sessionStorage.clear();
            });
        }
    });
});
