#!/bin/sh

# self-certificate command comes from here
# https://letsencrypt.org/docs/certificates-for-localhost/#making-and-trusting-your-own-certificates


REV_PROXY_DEP_DIR="$1"
LOCALHOST_CRT="$2"
LOCALHOST_KEY="$3"

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

openssl req -x509 -out "$LOCALHOST_CRT" -keyout "$LOCALHOST_KEY" \
-newkey rsa:2048 -nodes -sha256 -subj '/CN=localhost' \
-extensions EXT -config "$TMP_CONF_FILE"

chmod +r $LOCALHOST_CRT
chmod +r $LOCALHOST_KEY

rm "$TMP_CONF_FILE"
