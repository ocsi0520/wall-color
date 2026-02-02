Project Initialization
----------------------
Mentioned at [get started](../README.md#to-get-started) in order to initialize the project, you need one thing: run _project-init.sh_ in a POSIX-compliant terminal. Since the file is a simple [shell script](https://en.wikipedia.org/wiki/Shell_script), so no fancy bash syntax is used, you can safely use it on MacOS as well.

There are 3 things that needs to be done in order to run the application.

Environment variables
----------------------
By default both [Docker](https://docs.docker.com/compose/how-tos/environment-variables/variable-interpolation#env-file) and Podman compose reads the _.env_ file next to its [base compose file](./local-dev.md).
As it can happen you forgot to create your own _.env_ file from _env-example.txt_ file, _env-init.sh_ does this for you exactly. It doesn't override the existing one, but it creates _.env file_ for you if absent.

JWT for Backend
---------------
Backend uses [JWT](https://www.jwt.io/introduction#what-is-json-web-token) for [authentication and authorization](https://www.okta.com/identity-101/authentication-vs-authorization/).
I'm specifically using a [JWS](https://dev.to/mayank_tamrkar/jwt-vs-jws-vs-jwe-whats-the-difference-when-to-use-each-16l2) with [asymmetric keys](https://www.softoneconsultancy.com/complete-guide-to-jwt-authentication-with-asymmetric-keys/). Therefore we need to generate the public-private key-pairs which is done in _jwt-cert-init.sh_ and placed into _color-manager/src/main/resources/certs_. Then Spring Boot picks it up through classpath.
**This should be changed** and there's already [a ticket about it](https://github.com/ocsi0520/wall-color/issues/51).

Https for Reverse Proxy
----------------------
For the reverse proxy we need to have a TLS certificate. Most basic approach is to create a self-signed certificate, which is exactly done by _cert-init.sh_ and placed into _reverse-proxy/deployment/certs_ folder.
The code for creating the self-signed certificate is copied from [Let's Encrypt's website](https://letsencrypt.org/docs/certificates-for-localhost/#making-and-trusting-your-own-certificates) but with one change:
In simple shell [process substitution](https://www.gnu.org/software/bash/manual/html_node/Process-Substitution.html) is not available, therefore I needed to create a separate temporary config file.
