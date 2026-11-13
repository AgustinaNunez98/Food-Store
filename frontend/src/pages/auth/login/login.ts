import type { IUser } from "../../../types";

const form = document.querySelector<HTMLFormElement>("#form-login");

if(form){
    form.addEventListener("submit", (event)=>{
        event.preventDefault();
    
        const email = document.querySelector<HTMLInputElement>("#email");
        const password = document.querySelector<HTMLInputElement>("#password");


        if (email && password) {
            const emailValue = email.value;
            const passwordValue = password.value;
            
            const users = JSON.parse(localStorage.getItem("users") || "[]") as IUser[];

            const usuarioEncontrado = users.find(
            user => 
                user.email  === emailValue && 
            user.password === passwordValue);
            
            if (usuarioEncontrado) {
                localStorage.setItem("userData", JSON.stringify(usuarioEncontrado));
                alert("Inicio de sesión exitoso");
                window.location.href = "/src/pages/client/home/home.html";
            } else {
                alert("Credenciales inválidas");
            }
            console.log(emailValue);
            console.log(passwordValue);
            console.log(users);
            console.log(usuarioEncontrado);
        }

    });
}