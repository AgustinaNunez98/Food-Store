import type { IUser } from "./types";

const userData = JSON.parse(localStorage.getItem("userData") || "null" ) as IUser | null;

if (!userData) {
    window.location.href = "/src/pages/auth/login/login.html"; //revisar para mejorar la ruta
} else {
    const urlActual = window.location.pathname;
    const userRol = userData.rol;
    if (userRol !== "admin" && urlActual.endsWith("/src/pages/admin/admin.html")) { //revisar para mejorar la ruta
        window.location.href = "/src/pages/client/home/home.html"; //revisar para mejorar la ruta
    }
    else if (userRol === "admin") {
        console.log("Acceso de administrador concedido.");
    }
    else if (urlActual === "/src/pages/client/home/home.html") { //revisar para mejorar la ruta
        console.log("Acceso concedido.");
    }
}
