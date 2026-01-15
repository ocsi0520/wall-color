#!/bin/sh

CMD="podman-compose \
  -f project-compose.local.yaml \
  -f ./postgresdb/deployment/podman-compose.local.yaml \
  -f ./color-manager/deployment/podman-compose.local.yaml \
  -f ./color-angular-frontend/deployment/podman-compose.local.yaml"

case "$1" in
  "up")
    $CMD up
    ;;
  "up-build")
    $CMD up --build
    ;;
  "down")
    $CMD down
    ;;
  *)
    echo "Usage: $0 {up|up-build|down}"
    ;;
esac
