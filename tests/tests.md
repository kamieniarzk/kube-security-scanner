## Test scenarios
### Misconfigurations
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
  'http://localhost:8082/api/scans/aggregated/65ae8b2dc576fc0b1d1357bb/result' \
  -H 'accept: text/csv'
```

Result filter JSON
```json
{
  "namespace": [
    "default", "kube-system", "kube-public", "kube-node-lease", "gmp-public", "gmp-system", "gke-managed-system", "local-path-storage"
  ]
}
```

### Vulnerabilities
```shell
curl -X 'POST' \
  'http://localhost:8081/api/scans/trivy' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "scanners": [
    "vuln"
  ],
  "severityFilter": [
    "MEDIUM", "HIGH", "CRITICAL"
  ]
}'
```

Result filter JSON
```json
{
  "namespace": [
    "default", "kube-system", "kube-public", "kube-node-lease", "gmp-public", "gmp-system", "gke-managed-system", "local-path-storage"
  ]
}
```

### Compliance (CIS)
```shell
curl -X 'POST' \
  'http://localhost:8081/api/scans/trivy' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "compliance": "k8s-cis"
}'
```

```shell
curl -X 'POST' \
  'http://localhost:8081/api/scans/kube-bench' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{}'
```

```shell
curl -X 'POST' \
  'http://localhost:8081/api/kubescape/runs' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "frameworks": [
    "cis-generic"
  ]
}'
```
