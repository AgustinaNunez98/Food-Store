import type {ICartItem} from '../types/product';

export function getCart (): ICartItem[] {
    const cart = localStorage.getItem('cart');
    if (!cart) {
        return [];
    } else {
        return JSON.parse(cart) as ICartItem[];
    }
}

export function saveCart (cart: ICartItem[]): void {
    localStorage.setItem('cart', JSON.stringify(cart));
}

export function addToCart (item: ICartItem): void {
    const cart = getCart();
    const existingItemIndex = cart.findIndex(cartItem => cartItem.id === item.id);
    if (existingItemIndex !== -1) {
        cart[existingItemIndex].cantidad += item.cantidad;
    } else {
        const newItem: ICartItem = {
            id: item.id,
            nombre: item.nombre,
            precio: item.precio,
            imagen: item.imagen,
            cantidad: item.cantidad
        };
        cart.push(newItem);
    }
    saveCart(cart); 
}

export function getCartTotal (): number {
    let total = 0;
    for (const item of getCart()) {
        const subtotal = item.precio * item.cantidad;
        total += subtotal;
    }
    return total;
}