# PDF to JSON Converter (Local with Docker)

This application allows you to upload a PDF file, convert it to Markdown using a local **Marker** OCR engine, and then parse the Markdown into a structured **JSON** using a local LLM via **Ollama**.

## Architecture

- **Frontend**: Angular 18+ (Standalone components), served by Nginx.
- **Backend**: Spring Boot 3.2+ (Java 17).
- **OCR Engine**: [Marker](https://github.com/datalab-to/marker) (Local Docker container).
- **LLM Engine**: [Ollama](https://ollama.com/) (Local Docker container).

## Prerequisites

- **Docker** and **Docker Compose** installed.
- (Optional) **NVIDIA GPU** for faster processing (configured in `docker-compose.yml`).

## Setup Instructions

### 1. Start the services

Run the following command in the project root:

```bash
docker-compose up -d --build
```

### 2. Pull the LLM Model

The backend is configured to use `llama3`. You need to pull it inside the Ollama container:

```bash
docker exec -it pdf-to-json-ollama-1 ollama pull llama3
```

*(Note: The container name might vary slightly depending on your directory name, use `docker ps` to check).*


## Accessing Services

- **Frontend**: Open `http://localhost` in your browser.
- **Backend API**: `http://localhost:8080/api/converter`.
- **OCR API**: `http://localhost:8000`.
- **Ollama API**: `http://localhost:11434`.

## GPU Support

By default, the `docker-compose.yml` is configured for **CPU-only** mode to ensure compatibility.

### If you have an NVIDIA GPU:
1.  Install the [NVIDIA Container Toolkit](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html).
2.  In `docker-compose.yml`, uncomment the `deploy` sections for both `marker-api` and `ollama` services.
3.  Restart the services: `docker-compose up -d --build`.
