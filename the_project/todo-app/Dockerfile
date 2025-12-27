FROM docker.io/babashka/babashka:1.12.213

LABEL org.opencontainers.image.source="https://github.com/timokramer/KubernetesSubmissions"
LABEL org.opencontainers.image.title="todo-app"
LABEL org.opencontainers.image.description="mooc.fi course on kubernetes"
LABEL org.opencontainers.image.version="1.4"

COPY main.clj .
ENTRYPOINT ["bb", "main.clj"]
