const cargarCategorias = (categorias) => {
    const listaCategorias = document.getElementById("lista-categorias");
    listaCategorias.innerHTML = "";
    categorias.forEach(categoria => {
        const elementoCategoria = document.createElement("li");
        const enlaceCategoria = document.createElement("a");
        enlaceCategoria.textContent = categoria;
        enlaceCategoria.href = "#";
        elementoCategoria.appendChild(enlaceCategoria);
        listaCategorias.appendChild(elementoCategoria);
    });
};

const cargarProductos = (productos) => {
    const contenedorProductos = document.getElementById("contenedor-productos");
    contenedorProductos.innerHTML = "";
    productos.forEach(producto => {
        contenedorProductos.insertAdjacentHTML("beforeend", `
            <article class="card mb-4">
                <img src="${producto.imagen}" class="card-img-top" alt="${producto.nombre}">
                <div class="card-body">
                    <h3 class="card-title">${producto.nombre}</h3>
                    <p class="card-text">${producto.descripcion}</p>
                    <p class="card-text">Precio: $${producto.precio}</p>
                    <button type="button" class="btn-agregar">Agregar</button>
                </div>
            </article>
        `);
    });

    const botonesAgregar = contenedorProductos.querySelectorAll(".btn-agregar");
    botonesAgregar.forEach((boton, indice) => {
        boton.addEventListener("click", () => {
            alert(`Agregaste ${productos[indice].nombre} al carrito.`);
        });
    });
};

const inicializarCatalogo = async () => {
    const datos = await obtenerDatos();
    cargarCategorias(datos.categorias);
    cargarProductos(datos.productos);
};

inicializarCatalogo();