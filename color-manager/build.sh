#!/usr/bin/env bash
set -e

GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'

PORT=4301
SPRING_CONTAINER_NAME="wall-color-backend"
SPRING_IMAGE_NAME="${SPRING_CONTAINER_NAME}-image"

podman stop $SPRING_CONTAINER_NAME 2>/dev/null || true
podman rm $SPRING_CONTAINER_NAME 2>/dev/null || true
podman image rm $SPRING_IMAGE_NAME 2>/dev/null || true

echo -e "${GREEN_COLOR}Removed previous container and image${NO_COLOR}"

podman build -f deployment/Containerfile -t $SPRING_IMAGE_NAME .
echo -e "${GREEN_COLOR}Built $SPRING_IMAGE_NAME${NO_COLOR}"

podman run \
    -d \
    --name $SPRING_CONTAINER_NAME \
    -p ${PORT}:${PORT} \
    -e APP_PORT=${PORT} \
    $SPRING_IMAGE_NAME

echo -e "${GREEN_COLOR}$SPRING_CONTAINER_NAME is running on port $PORT${NO_COLOR}"
