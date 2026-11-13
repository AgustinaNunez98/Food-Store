import {PRODUCTS, getCategories} from "../../../data/data";
import type {IProduct, ICartItem} from "../../../types/product";
import type {ICategoria} from "../../../types/categoria";
import { addToCart } from "../../../utils/cart";

const buscador = document.querySelector<HTMLInputElement>("#buscador");
const listaCategorias = document.querySelector<HTMLUListElement>("#lista-categorias");
const contenedorProductos = document.querySelector<HTMLElement>("#contenedor-productos");

const productos = PRODUCTS;
const categorias = getCategories();

export function renderProducts(products: IProduct[]) {
    contenedorProductos!.innerHTML = "";
    if (products.length === 0) {
        contenedorProductos!.innerHTML = "<p>No se encontraron productos.</p>";
        return;
    }
    products.forEach((product) => {
        const productCard = document.createElement("div");
        productCard.className = "product-card";
        productCard.innerHTML = `
            <img src="${product.imagen}" alt="${product.nombre}">
            <h3>${product.nombre}</h3>
            <p>${product.descripcion}</p>
            <p>Precio: $${product.precio}</p>
            <button class="add-to-cart" data-id="${product.id}">Agregar al carrito</button>
        `;
        contenedorProductos!.appendChild(productCard);
    });

    const addToCartButtons = document.querySelectorAll<HTMLButtonElement>(".add-to-cart");

    for (const button of addToCartButtons) {
        button.addEventListener("click", () => {
            const productId = parseInt(button.dataset.id!, 10);
            const product = PRODUCTS.find(p => p.id === productId);
            if (product) {
                const cartItem: ICartItem = {
                    id: product.id,
                    nombre: product.nombre,
                    precio: product.precio,
                    imagen: product.imagen,
                    cantidad: 1
                };
                addToCart(cartItem);
            }
        })
    }
}

export function renderCategories(categories: ICategoria[]) {
    listaCategorias!.innerHTML = "";
    const allCategories = document.createElement("li");
    const allButton = document.createElement("button");
    allButton.textContent = "Todas";
    allButton.dataset.category = "Todas";

    allButton.addEventListener("click", () => {
        renderProducts(productos);
    });

    allCategories.appendChild(allButton);
    listaCategorias!.appendChild(allCategories);

    for (const category of categories) {
        const categoryItem = document.createElement("li");
        const categoryButton = document.createElement("button");
        categoryButton.textContent = category.nombre;
        categoryButton.dataset.category = category.nombre;

        categoryButton.addEventListener("click", () => {
            const filteredProducts = productos.filter(product => 
                product.categoria === category.nombre);
            renderProducts(filteredProducts);
        });

        categoryItem.appendChild(categoryButton);
        listaCategorias!.appendChild(categoryItem);
    }
}

if (buscador && listaCategorias && contenedorProductos) {
    renderProducts(productos);
    renderCategories(categorias);

    buscador.addEventListener("input", () => {
        const textBuscado = buscador.value.trim().toLowerCase();

        const productosFiltrados = productos.filter((product)=>
             product.nombre.toLowerCase().includes(textBuscado)
            );
        renderProducts(productosFiltrados);
    });
}

