import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Forwards API calls to the Spring Boot backend during development, mirroring the reverse
    // proxy that Apache HTTP Server will provide in production. The React app always calls
    // relative "/api/..." paths, so no environment-specific base URL branching is needed.
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
