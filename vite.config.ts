import { defineConfig } from "vite";
import { resolve } from "path";

export default defineConfig({
    base: "./",
    build: {
        rollupOptions: {
            input: {
                login: resolve(__dirname, "src/pages/auth/login/login.html"),
                registro: resolve(__dirname, "src/pages/auth/registro/registro.html"),
                home: resolve(__dirname, "src/pages/client/home/home.html"),
                cart: resolve(__dirname, "src/pages/client/cart/cart.html"),
                admin: resolve(__dirname, "src/pages/admin/admin.html")
            }
        }
    }
});