#!/bin/sh

# self-certificate command comes from here
# https://letsencrypt.org/docs/certificates-for-localhost/#making-and-trusting-your-own-certificates


REV_PROXY_DEP_DIR="$1"

DEV_CA="$REV_PROXY_DEP_DIR/devCA"
LOCALHOST="$REV_PROXY_DEP_DIR/localhost"


mkdir -p $REV_PROXY_DEP_DIR

# 1. Create dev CA
openssl genrsa -out "${DEV_CA}.key" 4096
openssl req -x509 -new -nodes -key "${DEV_CA}.key" -sha256 -days 1024 -out "${DEV_CA}.crt" -subj "/CN=Local Dev CA"

# 2. Create localhost cert signed by dev CA
openssl genrsa -out "${LOCALHOST}.key" 2048
openssl req -new -key "${LOCALHOST}.key" -out "${LOCALHOST}.csr" -subj "/CN=localhost"
openssl x509 -req -in "${LOCALHOST}.csr" -CA "${DEV_CA}.crt" -CAkey "${DEV_CA}.key" -CAcreateserial \
  -out "${LOCALHOST}.crt" -days 365 -sha256


chmod a+r "$REV_PROXY_DEP_DIR"/*