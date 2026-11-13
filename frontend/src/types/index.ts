export type Rol = "admin" | "client";

export interface IUser { 
    nombre: string;
    apellido: string;
    email: string;
    password: string;
    rol: Rol
}