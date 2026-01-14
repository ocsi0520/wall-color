#!/usr/bin/env bash
set -e

GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'

PORT=5432
DB_CONTAINER_NAME="wall-color-postgre"
DB_IMAGE_NAME="${DB_CONTAINER_NAME}-image"

podman stop $DB_CONTAINER_NAME 2>/dev/null || true
podman rm $DB_CONTAINER_NAME 2>/dev/null || true
podman image rm $DB_IMAGE_NAME 2>/dev/null || true

echo -e "${GREEN_COLOR}Removed previous container and image${NO_COLOR}"

sudo rm -rf ./volume/
mkdir volume
echo -e "${GREEN_COLOR}re-moved 'volume' folder volume data${NO_COLOR}"

podman build -f deployment/Containerfile -t $DB_IMAGE_NAME .
echo -e "${GREEN_COLOR}re-built ${DB_IMAGE_NAME} image${NO_COLOR}"

podman run -d --name ${DB_CONTAINER_NAME} \
    --network wall-color-net \
    --env-file=".env" \
    -v ./volume:/var/lib/postgresql \
    -p ${PORT}:${PORT} \
    $DB_IMAGE_NAME

echo -e "${GREEN_COLOR}$DB_CONTAINER_NAME container is running${NO_COLOR}"