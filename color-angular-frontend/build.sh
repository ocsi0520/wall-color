#!/usr/bin/env bash
set -e

GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'

PORT=4200
FRONTEND_CONTAINER_NAME="wall-color-frontend"
FRONTEND_IMAGE_NAME="${FRONTEND_CONTAINER_NAME}-image"

podman stop $FRONTEND_CONTAINER_NAME 2>/dev/null || true
podman rm $FRONTEND_CONTAINER_NAME 2>/dev/null || true
podman image rm $FRONTEND_IMAGE_NAME 2>/dev/null || true

echo -e "${GREEN_COLOR}Removed previous container and image${NO_COLOR}"

podman build -f deployment/Containerfile -t $FRONTEND_IMAGE_NAME .
echo -e "${GREEN_COLOR}re-built ${FRONTEND_IMAGE_NAME} image${NO_COLOR}"

podman run \
    -d --name $FRONTEND_CONTAINER_NAME \
    --network wall-color-net \
    -e NGINX_PORT=${PORT} \
    -p ${PORT}:${PORT} \
    $FRONTEND_IMAGE_NAME && \
echo -e "${GREEN_COLOR}$FRONTEND_CONTAINER_NAME container is running${NO_COLOR}"
