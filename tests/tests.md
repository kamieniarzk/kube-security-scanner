### Test 1 - Misconfigurations
```shell
curl -X 'POST' \
  'http://localhost:8082/api/scans/aggregated' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "kubescapeScanRequest": {
    "frameworks": [
      "allcontrols"
    ]
  },
  "trivyRunRequest": {
    "scanners": [
      "config", "rbac", "secret"
    ]
  },
  "kubeScoreScanRequest": {
  }
}'
```

```shell
curl -X 'GET' \
  'http://localhost:8082/api/scans/aggregated/65ae8b2dc576fc0b1d1357bb/result?namespace=default&namespace=kube-node-lease&namespace=kube-public&namespace=kube-system&severity=MEDIUM&severity=HIGH&severity=CRITICAL' \
  -H 'accept: text/csv'
```

### Test 2 - compliance (cis)




#### notes to self
* overlapping checks
* some checks do not work (either they require operator to be installed or private registry connection)
* different nodes (control plane and no control plane in gke and eks)
  https://aquasecurity.github.io/trivy/v0.22.0/vulnerability/examples/filter/
* 83 checks have been categorized
* storage in each cluster
* write about unit tests
* capitalization of `Node` and other resource names
