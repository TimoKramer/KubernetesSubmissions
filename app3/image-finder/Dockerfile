FROM docker.io/babashka/babashka:latest

LABEL org.opencontainers.image.source=https://github.com/timokramer/KubernetesSubmissions
LABEL org.opencontainers.image.title="image-finder"
LABEL org.opencontainers.image.description="mooc.fi course on kubernetes"
LABEL org.opencontainers.image.version="1.10"

COPY main.clj .
CMD ["bb", "main.clj"]
