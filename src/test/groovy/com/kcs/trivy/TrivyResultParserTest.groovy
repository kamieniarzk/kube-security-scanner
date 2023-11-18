package com.kcs.trivy


import spock.lang.Specification

class TrivyResultParserTest extends Specification {

  def "should properly parse full JSON output of trivy resources scan"() {
    given:
    var inputFile = new File("src/test/resources/trivy-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    var result = TrivyResultParser.parseFullResult(inputFileString)

    then:

    result.clusterName == 'gke_virtual-anchor-400608_europe-west1-b_cluster-1'
    result.resources.size() == 2

    with(result.resources.get(2)) {
      namespace == 'kube-system'
      kind == 'Pod'
      name == 'kube-proxy-gke-cluster-1-default-pool-98572dea-g8jl'
      results.size() > 0

      with(results.get(0).misconfigurations.get(0)) {
        type == 'Kubernetes Security Check'
        id == 'KSV009'
        avdid == 'AVD-KSV-0009'
        title == 'Access to host network'
        description == 'Sharing the host’s network namespace permits processes in the pod to communicate with processes bound to the host’s loopback adapter.'
        message == "Pod 'kube-proxy-gke-cluster-1-default-pool-98572dea-g8jl' should not set 'spec.template.spec.hostNetwork' to true"
        namespace == 'builtin.kubernetes.KSV009'
        query == 'data.builtin.kubernetes.KSV009.deny'
        resolution == "Do not set 'spec.template.spec.hostNetwork' to true."
        severity == 'HIGH'
        primaryUrl == 'https://avd.aquasec.com/misconfig/ksv009'
        references.size() == 2
        status == 'FAIL'
        causeMetadata.provider == 'Kubernetes'
        causeMetadata.startLine == 191
        causeMetadata.endLine == 262
      }
    }
  }

  def "should properly parse summary JSON output of trivy resources scan"() {
    given:
    var inputFile = new File("src/test/resources/trivy-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    var result = TrivyResultParser.parseSummaryResult(inputFileString)

    then:

    result.clusterName == 'gke_virtual-anchor-400608_europe-west1-b_cluster-1'
    result.resources.size() == 2

    with(result.resources.get(0)) {
      name == 'kcs-deployment'
      kind == 'Deployment'
      namespace == 'kcs'
      with(getResult()) {
        summary.successes == 74
        summary.failures == 0
        summary.exceptions == 0
      }
    }
  }
}
