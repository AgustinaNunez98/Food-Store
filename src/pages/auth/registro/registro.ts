import type { IUser } from "../../../types";

const form = document.querySelector<HTMLFormElement>("#form-registro");

if(form){
    form.addEventListener("submit", (event)=>{
        event.preventDefault();

        const nombre = document.querySelector<HTMLInputElement>("#nombre");
        const apellido = document.querySelector<HTMLInputElement>("#apellido");
        const email = document.querySelector<HTMLInputElement>("#email");
        const password = document.querySelector<HTMLInputElement>("#password");

        if(nombre && apellido && email && password) {
            const client: IUser = {
            nombre: nombre.value,
            apellido: apellido.value,
            email: email.value,
            password: password.value,
            rol: "client"
            };

            const users = JSON.parse(
                localStorage.getItem("users") || "[]"
            ) as IUser[];

            if (users.some(usuario => 
                usuario.email.trim().toLowerCase() === client.email.trim().toLowerCase())){
                alert("El email ya está registrado");
            } else {
                users.push(client);
                localStorage.setItem("users", JSON.stringify(users));
                alert("Usuario registrado con éxito");
            }

            console.log(client);
        }
    });
}