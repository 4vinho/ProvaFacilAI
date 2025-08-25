FROM ollama/ollama

RUN ollama serve & \
    sleep 5 && \
    ollama pull nomic-embed-text && \
    ollama pull qwen3:0.6b && \
    pkill ollama