#!/usr/bin/bash

podman stop wall-color-postgres-container;
podman rm wall-color-postgres-container;
sudo rm -rf ./volume/;

podman build -t wall-color-postgres-image .;
mkdir volume && \
podman run -d --name wall-color-postgres-container \
    --network wall-color-net \
    --env-file=".env" \
    -v ./volume:/var/lib/postgresql \
    -p 5432:5432 \
    wall-color-postgres-image
