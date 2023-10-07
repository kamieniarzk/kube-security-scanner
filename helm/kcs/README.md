<!--- app-name: kube-config-scanner -->

# kube-config-scanner helm chart

`kube-config-scanner` is a tool for security scan automation and analysis.

## Installing the Chart

To install the chart with the release name `my-release`:

```console
helm install my-release ./kcs -f ./kcs/values.yaml -n <my-namespace> --create-namespace
```

These commands deploy kube-config-scanner on the Kubernetes cluster in the default configuration.
The [Parameters](#parameters) section lists the parameters that can be configured during installation.

## Uninstalling the Chart

To uninstall/delete the `my-release` deployment:

```console
helm delete my-release
```

The command removes all the Kubernetes components associated with the chart and deletes the release.

## Parameters

### Common parameters

| Name                     | Description                                                                             | Value                               |
|--------------------------|-----------------------------------------------------------------------------------------|-------------------------------------|
| `image.repository`       | The repository of app container image                                                   | `"kamieniarzk/kube-config-scanner"` |
| `image.tag`              | App container image tag                                                                 | `"latest"`                          |
| `mongo.uri`              | Full MongoDB connection string URI                                                      | `"your-mongo-uri"`                  |
| `mongo.database`         | MongoDB database name                                                                   | `"your-mongo-database"`             |
| `logStorage.size`        | Size of persistent storage for in-cluster scan results                                  | `1Gi`                               |


Either put the parameters inside `values.yaml`

```console
helm install my-release -f ./kcs/values.yaml ./kcs
```

or specify each parameter using the `--set key=value[,key=value]` argument to `helm install`. For example,

```console
helm install my-release \
               --set mongo.uri=my-mongo-uri \
               --set mongo.database=my-mongo-database \
               --set logStorage.size=512Mi \
               ./kcs
```
> **Tip**: You can use the default [values.yaml](values.yaml)

### TODO
