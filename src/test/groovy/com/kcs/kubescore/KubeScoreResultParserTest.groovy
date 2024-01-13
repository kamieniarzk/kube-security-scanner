package com.kcs.kubescore

import spock.lang.Specification

class KubeScoreResultParserTest extends Specification {

  def "should properly parse top level objects"() {
    given:
    var inputFile = new File("src/test/resources/kube-score-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    var result = KubeScoreResultParser.parse(inputFileString)

    then:
    result.objects.size() == 3

    with(result.objects.get(0)) {
      name == 'kube-bench-h8x2m'
      kind == 'Pod'
      namespace == 'default'
    }

    with(result.objects.get(1)) {
      name == 'kube-hunter-prlqb'
      kind == 'Pod'
      namespace == 'default'
    }

    with(result.objects.get(2)) {
      name == 'kubernetes'
      kind == 'Service'
      namespace == 'default'
    }
  }

  def "should properly parse vulnerability groups"() {
    given:
    var inputFile = new File("src/test/resources/kube-score-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    var result = KubeScoreResultParser.parse(inputFileString)

    then:
    with(result.objects.get(0).vulnerabilities.get(0)) {
      severity == 'CRITICAL'
      title == 'Container Image Pull Policy'
      with(instances.get(0)) {
        title == '· kube-bench -> ImagePullPolicy is not set to Always'
        remediation.startsWith('It\'s recommended to always set the ImagePullPolicy to Always, to')
      }
    }

    with(result.objects.get(0).vulnerabilities.get(13)) {
      severity == 'CRITICAL'
      title == 'Pod Probes'
      with(instances.get(0)) {
        title == '· Container is missing a readinessProbe'
        remediation.startsWith('A readinessProbe should be used to indicate when the service is')
      }
    }

    with(result.objects.get(1).vulnerabilities.get(0)) {
      severity == 'CRITICAL'
      title == 'Container Resources'
      instances.size() == 4
      with (instances.get(3)) {
        title == '· kube-hunter -> Memory request is not set'
        remediation.endsWith('can start and run without crashing. Set resources.requests.memory')
      }
    }
  }

  def "should properly parse edge case example"() {
    given:
    var inputFile = new File("src/test/resources/kube-score-edge-case-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    var result = KubeScoreResultParser.parse(inputFileString)

    then:
    noExceptionThrown()
    result != null
  }
}
