#!/bin/sh

REV_PROXY_DEP_DIR="./reverse-proxy/deployment/certs"
LOCALHOST_CRT="$REV_PROXY_DEP_DIR/localhost.crt"
LOCALHOST_KEY="$REV_PROXY_DEP_DIR/localhost.key"

mkdir $REV_PROXY_DEP_DIR -p

if [ -f $LOCALHOST_CRT ] && [ -f $LOCALHOST_KEY ]; then
    echo "cert is already initialized for reverse-proxy"
    exit 0
fi

echo "cert for reverse-proxy is not initialized, about to generate"

TMP_CONF_CONTENT="
[dn]
CN=localhost
[req]
distinguished_name=dn
[EXT]
subjectAltName=DNS:localhost
keyUsage=digitalSignature
extendedKeyUsage=serverAuth
"

TMP_CONF_FILE=$(mktemp)
printf "%s\n" "$TMP_CONF_CONTENT" >> "$TMP_CONF_FILE"

openssl req -x509 -out "$LOCALHOST_CRT" -keyout "$LOCALHOST_KEY" -newkey rsa:2048 \
  -nodes -sha256 -subj '/CN=localhost' -extensions EXT -config "$TMP_CONF_FILE"

chmod +r $LOCALHOST_CRT
chmod +r $LOCALHOST_KEY

rm "$TMP_CONF_FILE"

echo "\
you might want to add the generated cert to trusted certs in your browser
check out this one i.e.: https://browserfy.net/index.php/2025/05/31/how-to-manage-your-ssl-certificates-in-brave/
"

echo "CRT is generated at $(realpath $LOCALHOST_CRT)"
