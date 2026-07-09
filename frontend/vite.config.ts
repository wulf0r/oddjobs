import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    host: '0.0.0.0',
    port: Number(process.env.VITE_PORT ?? 5173),
    proxy: {
      '/api': {
        target: process.env.VITE_BACKEND_PROXY_TARGET ?? 'http://localhost:9991',
        changeOrigin: true
      }
    },
    watch: {
      usePolling: true
    }
  }
})
