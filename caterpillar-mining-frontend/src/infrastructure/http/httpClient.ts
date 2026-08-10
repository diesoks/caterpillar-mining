import axios from 'axios'

// A single relative base URL is used so the exact same build works both against the Vite dev
// server proxy (see vite.config.ts) and behind an Apache reverse proxy in production - no
// environment-specific API URL configuration is needed.
export const httpClient = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
})
