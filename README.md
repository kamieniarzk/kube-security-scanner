## TODO
* ~~Make the service-account related to in-cluster app deployment have permissions to execute necessary actions~~
* ~~Check how kube-score can be used~~
* ~~Embed kube-score binary within app docker image~~
* ~~Use MongoDB to persist scan related data~~
* Use ephemeral pod storage (emptyDir) to store YAML manifests for kube-score
* Use PVC to persist logs from scan runs 
  * make compatible local-dev configuration
* (nice to have): configure GitHub workflow to build and push docker image

### References
* [kube-hunter](https://github.com/aquasecurity/kube-hunter)
* [kube-bench](https://github.com/aquasecurity/kube-bench)
* [kube-score](https://github.com/zegl/kube-score)
