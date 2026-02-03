Reverse proxy (compose-only)
----------------------------
This is the minimal reverse proxy setup. As described before, it listens on port **2000** and supports **TLS**, **HTTP/1.1**, **HTTP/2**, and **HTTP/3**.

Plain HTTP requests are automatically redirected to HTTPS. \
Requests to `/api/` are proxied to the Spring Boot application on port **8080**, while all other traffic is forwarded to the frontend development server on port **4200**.

WebSocket connections are also proxied, which is required for Hot Module Replacement (HMR). Since Angular uses **Vite** as its default bundler starting with v17 ([announcement](http://blog.angular.dev/introducing-angular-v17-4d7033312e4b#879d), [migration guide](https://angular.dev/tools/cli/build-system-migration)), the reverse proxy must forward the appropriate WebSocket headers expected by Vite.

The exact steps used to enable WebSocket forwarding are documented [here](https://aronschueler.de/blog/2024/07/29/enabling-hot-module-replacement-hmr-in-vite-with-nginx-reverse-proxy/).

HTTP/3 support was verified using:

```sh
podman run --rm --network host docker.io/alpine/curl-http3 curl --insecure --head --http3 https://localhost:2000
```

The `--insecure` flag is required because a self-signed certificate is used. Native curl was not used because HTTP/3 support is still [experimental](https://curl.se/docs/http3.html#experimental).

TLS certificates are generated automatically in the _reverse-proxy/deployment/certs_ directory and are excluded via [`.gitignore`](../.gitignore). These certificates are mounted into the Nginx container through `compose.local.yaml`.