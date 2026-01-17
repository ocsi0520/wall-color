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