#!/bin/sh

LIGHT_GREEN='\033[1;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

ask_allow_vibe_code() {
    while true; do
        echo "${RED}This part is vibe-coded. PLEASE CHECK IT BEFORE ACCEPTING.${NC}"
        echo "---\n${LIGHT_GREEN}$(cat $1)\n${NC}---\n"
        echo "${RED}Do you accept this piece of code to run?${NC} (y/n)"
        read answer

        case "$answer" in
            y|Y)
                return 0  # true
                ;;
            n|N)
                return 1  # false
                ;;
            *)
                echo "Invalid input. Try again."
                ;;
        esac
    done
}

if ! [ -f ".env" ] ; then
    tail env-example.txt -n +4 > .env
    echo ".env is created from env-example.txt"
else
    echo ".env exists already"    
fi

RESOURCES_DIR="./color-manager/src/main/resources/certs"
PRIVATE_KEY="$RESOURCES_DIR/private.pem"
PUBLIC_KEY="$RESOURCES_DIR/public.pem"

if ! [ -d "$RESOURCES_DIR" ]; then
    mkdir $RESOURCES_DIR;
    echo "created $RESOURCES_DIR directory"
fi

if ! [ -f "$PRIVATE_KEY" ] || ! [ -f "$PUBLIC_KEY" ]; then
    echo "public-private pairs are not present, about to create"
    openssl genrsa 4096 > $PRIVATE_KEY
    openssl rsa -in $PRIVATE_KEY -pubout -out $PUBLIC_KEY
    echo "public-private pairs are created in $RESOURCES_DIR"
else
    echo "public-private keys already exist"
fi


REV_PROXY_DEP_DIR="./reverse-proxy/deployment/certs"

if ! [ -d $REV_PROXY_DEP_DIR ]; then
    echo "cert for reverse-proxy is not initialized, about to generate"
    CERT_INIT_FILE="./project-init/cert-init.sh"
    if ask_allow_vibe_code "$CERT_INIT_FILE"; then
        sh "$CERT_INIT_FILE" "$REV_PROXY_DEP_DIR"
    else
        echo "make sure cert is generated"
    fi

else
    echo "cert is already initialized for reverse-proxy"
fi