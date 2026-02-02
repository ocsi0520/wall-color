Local development and deployment
-------------------------------
Primarily I'm using podman and podman-compose, and trying to be compliant with [OCI](https://opencontainers.org/).
Regarding deployment there are 4 kinds of files:
- Containerfile: Defines the production container image. These are [multi-stage](https://docs.docker.com/build/building/multi-stage/) builds, so eventually the images contain only the object/minimized code.
- Containerfile.local: Defines the container image used for local development (Compose environment). It is always tagged as `local`, as enforced by the compose.local.yaml files.
- compose.local.yaml: Defines how individual application components (DB, FE, BE) are included in the local Compose environment, including volumes, required environment variables, and exposed ports.
- YAML files under the _helm-chart_ directory: Kubernetes manifests with templating/interpolation support, used for deploying the application to a Kubernetes cluster.

In compose environment, there's a "base" file, called [_project-compose.local.yaml_](../project-compose.local.yaml) which sole purpose is to set the base for relative paths to the root of the project.
For more details check out [Docker's docs](https://docs.docker.com/compose/intro/compose-application-model/#the-compose-file:~:text=Relative%20paths%20are%20resolved%20based%20on%20the%20first%20Compose%20file%27s%20parent%20folder).

How do containers see changes and react?
----------------------------------------
All compose files use the corresponding `Containerfile.local` which starts the application components in dev/watcher mode. In order to see source file changes on the host from the container's perspective, we need to [bind-mount](https://docs.docker.com/engine/storage/bind-mounts/) those files into the container. In case there are some overlaps between the mounted data and the copied data during image initialization, the [mounted data takes precedence](https://docs.docker.com/engine/storage/bind-mounts/#bind-mounting-over-existing-data).

Frontend part
---------------
The _node\_modules_ folder often differs between the host and the container. The reasons include differences in the host and container:
- operating system
- CPU architecture (less common, but emulation is possible)
- Node.js version
- package manager and its version
- platform-specific or optional dependencies
- postinstall scripts (e.g., compiling native code)

Because the _node\_modules_ folder differs, bind-mounting the project directory from the host will cause the host’s _node\_modules_ directory to obscure (hide) the container’s _node\_modules_ directory.

To prevent this, a separate anonymous volume should be used for _node\_modules_.

Backend part (`:Z` flag)
-------------------------
There's a difference between [Vite's hot-module-replacement](https://vite.dev/guide/features#hot-module-replacement) and [Spring Boot's Hot Swapping](https://docs.spring.io/spring-boot/how-to/hotswapping.html).
Meanwhile Frontend only needs to read the source files, Backend needs to write `.class` files to _target_ folder.

There are extra protection layers such as [SELinux](https://www.redhat.com/en/topics/linux/what-is-selinux) and [AppArmor](https://www.apparmor.net/) which can prevent processes from accessing resources such as files, or can restrict their access modes. In case you have enabled SELinux, then you need something that re-labels ([Docker docs](https://docs.docker.com/engine/storage/bind-mounts/#configure-the-selinux-label), [Podman docs](https://docs.podman.io/en/v4.4/markdown/options/volume.html#:~:text=Labeling%20Volume%20Mounts)) the files from the perspective of SELinux. That's exactly what `:z` and `:Z` does. `:z` is allowing access for multiple containers, meanwhile `:Z` restricts to one container. (Actually AppArmor differs, as it does not hold labels for files, rather it assigns profiles to the processes - process-side confinement.)

To really deep dive into labeling of SELinux, check out [this one]((https://docs.redhat.com/en/documentation/red_hat_enterprise_linux/8/epub/using_selinux/assembly_using-multi-category-security-mcs-for-data-confidentiality_using-selinux#introduction-to-selinux_getting-started-with-selinux)), and you can dig in AppArmor [here](https://gitlab.com/apparmor/apparmor/-/wikis/About#:~:text=The%20base%20unit%20of%20AppArmor%20confinement%20is%20a%20profile).

Additionally what is `:U` flag?
-------------------------------
Just to be inclusive, let's mention `:U` flag as well. Meanwhile `:z|:Z` flags only changes SELinux-related labels about the files, `:U` changes the file's owner to the currently logged-in user in the container.
Be cautious with it.

Database part
-------------
The only reason I have a separate Containerfile.local for Postgre is to copy [seed data](../postgresdb/seed/). If you place sql scripts under _/docker-entrypoint-initdb.d/_ folder, then Postgre container automatically executes them.
More details on their [official docker image site](https://hub.docker.com/_/postgres#initialization-scripts).
