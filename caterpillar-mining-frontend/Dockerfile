# ---- Build stage: compile the React app to static files ----
FROM node:22-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

# ---- Runtime stage: Apache HTTP Server serves the build + proxies /api to the backend ----
FROM httpd:2.4-alpine
COPY --from=build /app/dist/ /usr/local/apache2/htdocs/
COPY apache/httpd.conf /usr/local/apache2/conf/httpd.conf

EXPOSE 80
