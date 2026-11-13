import {getCart, getCartTotal} from "../../../utils/cart";

const contenedorItems = document.querySelector<HTMLElement>("#cart-items");
const elementoTotal = document.querySelector<HTMLElement>("#cart-total");

const itemsCarrito = getCart();
const totalCarrito = getCartTotal();

console.log("Items en el carrito:", itemsCarrito);
console.log("Total del carrito:", totalCarrito);

if (contenedorItems) {
    contenedorItems.innerHTML = "";
    if (itemsCarrito.length === 0) {
        contenedorItems.innerHTML = "<p>El carrito está vacío.</p>";
    } else {
        itemsCarrito.forEach((item) => {
            const subtotal = item.precio * item.cantidad;
            const elementoItem = document.createElement("div");
            elementoItem.className = "cart-item";
            elementoItem.innerHTML = `
                <h3>${item.nombre}</h3>
                <p>Precio: $${item.precio}</p>
                <p>Cantidad: ${item.cantidad}</p>
                <p>Subtotal: $${subtotal}</p>
            `;
            contenedorItems.appendChild(elementoItem);
        })
    }
}

if (elementoTotal) {
    elementoTotal.textContent = `Total: $${totalCarrito}`;
}