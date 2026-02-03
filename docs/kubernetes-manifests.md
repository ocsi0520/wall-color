Kubernetes deployment
---------------------
As aformentioned in the main docs, you need Kubernetes and Helm. I created the most basic _helm-chart_.
To use it you need to open a terminal at wall-color folder and type:
```sh
helm install <release name you pick> ./helm-chart/ --values ./helm-chart/values.yaml
# i.e.
helm install wall-color-release ./helm-chart/ --values ./helm-chart/values.yaml
```
_values.yaml_ is the same as env-example.txt: it's just an example, please change values, or rather use your own yaml file.
Also consider configuring [encryption at-rest](https://kubernetes.io/docs/tasks/administer-cluster/encrypt-data/) for Secrets.

In order to revoke deployment just use `uninstall` command:

```sh
helm uninstall <release name you pick>
# i.e.
helm uninstall wall-color-release
```

Kubernetes components
---------------------
In case you checked the [general architecture](../README.md#general-architecture), then there's nothing that would surprise you:

```mermaid
flowchart LR
    ING[Nginx Ingress]

    subgraph FE_GROUP["Frontend (Angular + Nginx)"]
        FE_SVC[FE Service]
        FE1[FE-1]
        FE2[FE-2]
        FE_SVC --> FE1
        FE_SVC --> FE2
    end

    subgraph BE_GROUP["Backend (Spring Boot)"]
        BE_SVC[BE Service]
        BE1[BE-1]
        BE2[BE-2]
        BE_SVC --> BE1
        BE_SVC --> BE2
    end

    subgraph DB_GROUP["Database (CloudNativePG)"]
        DB_RW[DB RW Service]
        DB_RO[DB RO Service]
        DB1[Primary]
        DB2[Replica]
        DB_RW --> DB1
        DB_RO --> DB2
    end

    ING -- /api prefixed calls --> BE_SVC
    ING -- non-api calls --> FE_SVC

    BE1 --> DB_RW
    BE2 --> DB_RW

    BE1 --> DB_RO
    BE2 --> DB_RO
```

Database
--------
As you can see each service has 2 replicas. For stateless services, such as frontend and backend, there's not much complexity to add when running them on multiple instances (pods).

However, in case of a database, that's a whole other story. If we use [deployment](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
the same way as we did with stateless services, we would end up with basically two different database instances, with two different [persistent volumes](https://kubernetes.io/docs/concepts/storage/persistent-volumes/), which do not communicate with each other. Query 'A' goes to one, query 'B' goes to the other instance, leaving an inconsistent state.

[Statefulset](https://kubernetes.io/docs/concepts/workloads/controllers/statefulset/) per se wouldn't help much. It would provide ordered unique network identifiers, but the main problem remains:
Database instances do not communicate with each other. The solution for this is tedious and complex if we want to tackle it by hand, luckily we already have a solution out-of-box: [Operator pattern](https://kubernetes.io/docs/concepts/extend-kubernetes/operator/).

CAP
---
When we have a distributed database system, we strive for 3 things:
- consistency among the instances
- high availability
- resilience against node failure

As [CAP theorem](https://en.wikipedia.org/wiki/CAP_theorem) says we can only pick two, although we can make compromises.
An operator handles these compromises for us.

CloudNativePG
-------------
There are dozens of pre-written operators for PostgreSQL, the one that I picked is [CloudNativePG](https://cloudnative-pg.io/).

As with the simple [Postgre container](./local-dev.md#database), we would like to have an initial database structure (we can leave out initial data, we only need the schema.)
The initialization is quite similar, but still differs in a significant way: Instead of copying sql files, we need to reference ConfigMaps or Secrets containing SQL commands in [`postInitApplicationSQLRefs`](https://cloudnative-pg.io/documentation/1.17/api_reference/#postinitapplicationsqlrefs).

Since we are using Helm that's doable by including _00_schema.sql_ file with [`.Files.Get`](https://helm.sh/docs/chart_template_guide/accessing_files) Go template.
I'm keen on single source of truth. Neverthless, Helm can't read files beyond its scope (_helm-chart_ folder), so directly I wasn't able to reach _postgresdb/seed/00_schema.sql_. The solution was to create a relative symbolic link in the folder. Through this symbolic link the file could be read and included into _00_schema.sql.yaml_ file.


Summary of ports
----------------

| Service       | Port                  |
| ------------- | ----------------------|
| Frontend      | TCP: 4200             |
| Backend       | TCP: 8080             |
| Database      | TCP: 5432             |
| Ingress       | TCP/UDP: 443, TCP: 80 |



