# Food Store

Aplicación web de una tienda de comida desarrollada como trabajo práctico
integrador de Programación 3.

## Tecnologías utilizadas

- HTML
- CSS
- TypeScript
- Vite
- LocalStorage

## Funcionalidades

- Registro de usuarios.
- Inicio de sesión.
- Catálogo de productos.
- Búsqueda de productos por nombre.
- Filtrado de productos por categoría.
- Carrito de compras persistente mediante `localStorage`.
- Cálculo de cantidades, subtotales y total del carrito.

## Requisitos

- Node.js
- npm

## Instalación

Desde la carpeta del proyecto, instalar las dependencias:

```bash
npm install
```

## Ejecución en desarrollo

Iniciar el servidor local con:

```bash
npm run dev
```

Luego abrir la dirección mostrada por Vite en el navegador.

## Compilación para producción

Para generar la versión de producción:

```bash
npm run build
```

Los archivos compilados se generan dentro de la carpeta `dist`.

## Rutas principales

- Catálogo principal: `/src/pages/client/home/home.html`
- Inicio de sesión: `/src/pages/auth/login/login.html`
- Registro: `/src/pages/auth/registro/registro.html`
- Carrito: `/src/pages/client/cart/cart.html`

## Estructura principal

```text
src/
├── data/              Datos de los productos
├── pages/             Páginas de autenticación y cliente
├── types/             Interfaces y tipos de TypeScript
└── utils/             Lógica reutilizable del carrito y sesión
```

## Enlace video

[Ver presentación del proyecto en YouTube](https://youtu.be/anmHyeXLndY)
