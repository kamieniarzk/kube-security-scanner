# PV based log storage

## Status

Accepted

## Context

There is a need for persisting log output of jobs scheduled by kube-security-scanner.
The need is motivated by the fact that those logs are usually ephemeral (unless some cluster-wide logging solution is configured but that would make kube-security-scanner depend on it and introduce unnecessary coupling).

## Decision

The decision is to simply store the logs in a Persistent Volume mounted on the pod filesystem.

### Possible alternatives

* Integration with external solutions present in the cluster (such as ELK stack)
* Not persist logs at all - run any sort of analysis live and store the results in synthesised form
* Use some sort of database like Elasticsearch

## Consequences
### Pros
* Easiness
* No additional coupling (database, external integrations)

### Cons
* Unfortunately, this requires the cluster to have a storage class capable of provisioning ReadWriteMany volumes (possible hack for the future - allow ReadWriteOnce or emptyDir for clusters without proper storageClasses)
