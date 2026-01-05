FROM docker.io/babashka/babashka:1.12.213

LABEL org.opencontainers.image.source="https://github.com/timokramer/KubernetesSubmissions"
LABEL org.opencontainers.image.title="hash-response"
LABEL org.opencontainers.image.description="mooc.fi course on kubernetes"
LABEL org.opencontainers.image.version="1.5"

COPY main.clj .
CMD ["bb", "main.clj"]
