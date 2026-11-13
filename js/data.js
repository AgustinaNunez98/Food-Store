// Array de categorías
const categorias = ["Hamburguesas", "Pizzas", "Papas Fritas", "Bebidas"];
const productos = [
    {
        id: 1,
        nombre: "Smash Burger Bacon",
        descripcion: "Doble carne smash, cheddar, bacon crocante y salsa de la casa",
        precio: 12500,
        imagen: "assets/Smash burger.jpeg",
        categoria: "Hamburguesas"
    },
    {
        id: 2,
        nombre: "Pizza Pepperoni",
        descripcion: "Muzzarella, pepperoni y salsa de tomate casera",
        precio: 14000,
        imagen: "assets/Pizza Pepperoni.jpeg",
        categoria: "Pizzas"
    },
    {
        id: 3,
        nombre: "Papas Cheddar",
        descripcion: "Papas fritas crocantes con cheddar y bacon",
        precio: 6500,
        imagen: "assets/Papas Cheddar.jpeg",
        categoria: "Papas Fritas"
    },
    {
        id: 4,
        nombre: "Gaseosa Personal",
        descripcion: "Bebida fría de 500 ml para acompañar tu pedido",
        precio: 2500,
        imagen: "assets/gaseosa.jpeg",
        categoria: "Bebidas"
    }
];

const obtenerDatos = async () => ({ categorias, productos });