#!/bin/sh

if command -v podman >/dev/null 2>&1; then
    COMPOSE_CMD="podman compose"
elif command -v docker >/dev/null 2>&1; then
    COMPOSE_CMD="docker compose"
else
    echo "Error: neither podman nor docker is installed." >&2
    exit 1
fi

CMD_WITH_YAMLS="${COMPOSE_CMD} \
  -f project-compose.local.yaml \
  -f ./reverse-proxy/deployment/podman-compose.local.yaml \
  -f ./postgresdb/deployment/podman-compose.local.yaml \
  -f ./color-manager/deployment/podman-compose.local.yaml \
  -f ./color-angular-frontend/deployment/podman-compose.local.yaml"

case "$1" in
  "up")
    $CMD_WITH_YAMLS up
    ;;
  "up-build")
    $CMD_WITH_YAMLS up --build
    ;;
  "down")
    $CMD_WITH_YAMLS down
    ;;
  *)
    echo "Usage: $0 {up|up-build|down}"
    ;;
esac
