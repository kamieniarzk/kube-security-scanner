## TODO
* ~~Make the service-account related to in-cluster app deployment have permissions to execute necessary actions~~
* ~~Check how kube-score can be used~~
* ~~Embed kube-score binary within app docker image~~
* ~~Use MongoDB to persist scan related data~~
* ~~Use ephemeral pod storage (emptyDir) to store YAML manifests for kube-score~~
* ~~Use PVC to persist logs from scan runs~~
  * ~~make compatible local-dev configuration~~
* ~~(nice to have): configure GitHub workflow to build and push docker image~~
* ~~Implement kube-score service layer~~
  * ~~take resource name and namespace~~
  * ~~run kube-score binary on that resource yaml stored on temp PV~~
  * ~~save kube-score output along with other logs (perhaps use distinct location)~~
* ~~Refactor service and persistence layer - introduce distinct interfaces for each type of scans (hunter, score, bench)~~
* ~~Initialize kube-hunter interface~~
  * ~~one persistence layer for JobRuns (id, date, podName)~~
  * ~~separate persistence layers for each kube-bench and kube-hunter (both job based tasks)~~
* ~~Init context aware analysis~~
  * ~~parse kube-hunter json to object~~
  * ~~parse kube-bench to some object structure~~
  * ~~parse kube-score to some object structure~~
* Create helm chart
  * create templates from objects in `k8s` dir
  * create Values.yaml
  * create variable identifying cluster in `Values.yaml`
* Cluster context
  * ~~recognize cluster type (for different kube-bench config and possibly other things too)~~
  * set `clusterId` variable (from value passed by `Values.yaml`, if not present fallback to some value based on runtime, e.g. node name or api server url)
  * save `clusterId` in all persisted runs



### References
* [kube-hunter](https://github.com/aquasecurity/kube-hunter)
* [kube-bench](https://github.com/aquasecurity/kube-bench)
* [kube-score](https://github.com/zegl/kube-score)
