GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'

PORT=4200
FRONTEND_CONTAINER_NAME="wall-color-frontend"
FRONTEND_IMAGE_NAME="${FRONTEND_CONTAINER_NAME}-image"

podman stop $FRONTEND_CONTAINER_NAME;
podman rm $FRONTEND_CONTAINER_NAME;
echo -e "${GREEN_COLOR}removed ${FRONTEND_CONTAINER_NAME} container${NO_COLOR}"

podman image rm $FRONTEND_IMAGE_NAME
echo "removed ${FRONTEND_IMAGE_NAME} image"
podman build -f deployment/Containerfile -t $FRONTEND_IMAGE_NAME . && \
echo -e "${GREEN_COLOR}re-built ${FRONTEND_IMAGE_NAME} image${NO_COLOR}"

podman run \
    -e NGINX_PORT=${PORT} \
    -p ${PORT}:${PORT} \
    -d --name $FRONTEND_CONTAINER_NAME \
    $FRONTEND_IMAGE_NAME && \
echo -e "${GREEN_COLOR}$FRONTEND_CONTAINER_NAME container is running${NO_COLOR}"