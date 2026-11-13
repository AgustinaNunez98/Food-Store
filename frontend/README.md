# Food Store — Frontend

Aplicación web de una tienda de comida desarrollada para el Trabajo Práctico Integrador de Programación III.

## Tecnologías

HTML, CSS, TypeScript, Vite y `localStorage`.

## Funcionalidades

- Registro e inicio de sesión con datos locales.
- Catálogo con búsqueda por nombre y filtro por categoría.
- Carrito persistente en `localStorage`, con cantidades, subtotales y total.

## Requisitos

- Node.js
- npm

## Instalación y ejecución

Desde la carpeta `frontend/`, ejecutá:

```bash
npm install
npm run dev
```

Abrí la dirección que muestra Vite y agregale la ruta `/src/pages/auth/login/login.html`. Si es la primera vez que usás la aplicación, creá un usuario en la pantalla de registro.

## Rutas principales

| Pantalla | Ruta |
| --- | --- |
| Inicio de sesión | `/src/pages/auth/login/login.html` |
| Registro | `/src/pages/auth/registro/registro.html` |
| Catálogo | `/src/pages/client/home/home.html` |
| Carrito | `/src/pages/client/cart/cart.html` |

## Compilación para producción

Desde `frontend/`, ejecutá:

```bash
npm run build
```

Vite genera los archivos compilados en `frontend/dist/`.

## Estructura

- `src/data/` contiene los datos del catálogo.
- `src/pages/` contiene las pantallas de autenticación, cliente y administración.
- `src/types/` define los tipos de TypeScript.
- `src/utils/` contiene la lógica compartida del carrito y del cierre de sesión.
- `assets/` contiene las imágenes de los productos.
