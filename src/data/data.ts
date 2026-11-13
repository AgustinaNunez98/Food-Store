import type {IProduct} from "../types/product";
import type {ICategoria} from "../types/categoria";

export const PRODUCTS: IProduct[] = [
    {
        id: 1,
        nombre: "Smash Burger Bacon",
        descripcion: "Doble carne smash, cheddar, bacon crocante y salsa de la casa",
        precio: 12500,
        imagen: "/assets/Smash burger.jpeg",
        categoria: "Hamburguesas"
    },
    {
        id: 2,
        nombre: "Pizza Pepperoni",
        descripcion: "Muzzarella, pepperoni y salsa de tomate casera",
        precio: 14000,
        imagen: "/assets/Pizza Pepperoni.jpeg",
        categoria: "Pizzas"
    },
    {
        id: 3,
        nombre: "Papas Cheddar",
        descripcion: "Papas fritas crocantes con cheddar y bacon",
        precio: 6500,
        imagen: "/assets/Papas Cheddar.jpeg",
        categoria: "Papas Fritas"
    },
    {
        id: 4,
        nombre: "Gaseosa 500ml",
        descripcion: "Bebida fría de 500 ml para acompañar tu pedido",
        precio: 2500,
        imagen: "/assets/gaseosa.jpeg",
        categoria: "Bebidas"
    }
];

export function getCategories(): ICategoria[] {
    const categories : ICategoria[] = [];
    for (const product of PRODUCTS) {
        const categoria = product.categoria;
        const categoriaYaExiste = categories.some((category) => category.nombre === categoria);
        if (!categoriaYaExiste) {
            categories.push({ nombre: categoria });
        }
    }
    return categories;
}
