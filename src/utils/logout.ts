const logout =document.querySelectorAll<HTMLButtonElement>("#cerrar-sesion");

logout.forEach((button) => {
    button.addEventListener("click", () => {
        localStorage.removeItem("userData");
        window.location.href = "/src/pages/auth/login/login.html";
    });
});
