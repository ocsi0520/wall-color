Local development and deployment
--------------------------------
Local development is primarily based on **Podman** and **podman-compose**, with an emphasis on [OCI](https://opencontainers.org/) compatibility.

From a deployment perspective, the project consists of four main types of configuration files:

- **Containerfile**  
  Defines the production container image. These are [multi-stage builds](https://docs.docker.com/build/building/multi-stage/), resulting in minimal runtime images containing only compiled or optimized artifacts.

- **Containerfile.local**  
  Defines container images used for local development within a Compose environment. These images are always tagged as `local`, as enforced by the corresponding `compose.local.yaml` files.

- **compose.local.yaml**  
  Describes how individual application components (database, frontend, backend) are assembled in the local Compose environment, including volume mounts, required environment variables, and exposed ports.

- **helm-chart/** YAML files  
  Kubernetes manifests with templating and interpolation support, used for deploying the application to a Kubernetes cluster.

Within the Compose setup, a base file named [`project-compose.local.yaml`](../project-compose.local.yaml) is used solely to establish the project root as the reference point for resolving relative paths.  
For details on Compose path resolution, see [Docker’s documentation](https://docs.docker.com/compose/intro/compose-application-model/#the-compose-file:~:text=Relative%20paths%20are%20resolved%20based%20on%20the%20first%20Compose%20file%27s%20parent%20folder).

How containers detect and react to changes
------------------------------------------
All Compose services use their corresponding `Containerfile.local`, which starts application components in development or watcher mode.

To make source code changes on the host visible inside containers, source directories are [bind-mounted](https://docs.docker.com/engine/storage/bind-mounts/) into the containers. When bind-mounted paths overlap with files copied into the image during build, the [mounted data takes precedence](https://docs.docker.com/engine/storage/bind-mounts/#bind-mounting-over-existing-data).

Frontend
--------
The `node_modules` directory frequently differs between the host and the container due to variations in:

- operating system
- CPU architecture (less common, but possible via emulation)
- Node.js version
- package manager and its version
- platform-specific or optional dependencies
- `postinstall` scripts (e.g. native code compilation)

Because of these differences, bind-mounting the entire project directory from the host would cause the host’s node_modules directory to obscure the container’s node_modules, which can result in dependency mismatches, installation failures, or hard-to-diagnose runtime crashes.

To avoid this, `node_modules` is stored in a separate anonymous volume inside the container rather than being bind-mounted from the host.

Backend (`:Z` flag)
-------------------
There is an important distinction between [Vite’s Hot Module Replacement](https://vite.dev/guide/features#hot-module-replacement) and [Spring Boot hot swapping](https://docs.spring.io/spring-boot/how-to/hotswapping.html).  
While the frontend only needs read access to source files, the backend must write compiled `.class` files to the `target` directory.

Additional security layers such as [SELinux](https://www.redhat.com/en/topics/linux/what-is-selinux) and [AppArmor](https://www.apparmor.net/) may restrict file access or access modes. When SELinux is enabled, bind-mounted files must be relabeled from SELinux’s perspective. This is handled via the `:z` and `:Z` volume mount options ([Docker docs](https://docs.docker.com/engine/storage/bind-mounts/#configure-the-selinux-label), [Podman docs](https://docs.podman.io/en/v4.4/markdown/options/volume.html)).

- `:z` allows shared access across multiple containers
- `:Z` restricts access to a single container

AppArmor behaves differently: instead of labeling files, it applies confinement profiles to processes (process-side confinement).

For a deeper explanation of SELinux labeling, see the Red Hat documentation on [Multi-Category Security (MCS)](https://docs.redhat.com/en/documentation/red_hat_enterprise_linux/8/epub/using_selinux/assembly_using-multi-category-security-mcs-for-data-confidentiality_using-selinux#introduction-to-selinux_getting-started-with-selinux).  
For AppArmor internals, refer to the [official overview](https://gitlab.com/apparmor/apparmor/-/wikis/About#:~:text=The%20base%20unit%20of%20AppArmor%20confinement%20is%20a%20profile).

Additionally: the `:U` flag
---------------------------
For completeness, the `:U` mount option is also worth mentioning. While `:z` and `:Z` only modify SELinux labels, `:U` changes file ownership to match the user inside the container.

Use this option with caution, as it modifies file ownership on the host.

Database
--------
A separate `Containerfile.local` is used for PostgreSQL solely to copy [seed data](../postgresdb/seed/).  
Any SQL scripts placed under `/docker-entrypoint-initdb.d/` are executed automatically when the PostgreSQL container initializes.

Further details are available in the [official PostgreSQL Docker image documentation](https://hub.docker.com/_/postgres#initialization-scripts).
