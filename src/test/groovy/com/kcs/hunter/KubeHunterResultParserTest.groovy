package com.kcs.hunter

import com.kcs.hunter.result.KubeHunterResultParser
import spock.lang.Specification

class KubeHunterResultParserTest extends Specification {


  def "should properly parse kube-hunter result"() {
    given:
    var inputFile = new File("src/test/resources/kube-hunter-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    var result = KubeHunterResultParser.parse(inputFileString)

    then: 'nodes are parsed properly'
    result.getNodes().size() == 3
    with(result.getNodes().get(0)) {
      type == 'Node/Master'
      location == '10.42.1.1'
    }

    and: 'services are parsed properly'
    result.getServices().size() == 3
    with(result.getServices().get(2)) {
      service == 'API Server'
      location == '10.43.0.1:443'
    }

    and: 'vulnerabilities are parsed properly'
    result.getVulnerabilities().size() == 5
    with(result.getVulnerabilities().get(0)) {
      location == 'Local to Pod (kube-hunter-rc9xf)'
      vid == 'KHV050'
      category == 'Credential Access // Access container service account'
      severity == 'low'
      vulnerability == 'Read access to pod\'s service account token'
      description == 'Accessing the pod service account token gives an attacker the option to use the server API'
      evidence == 'eyJhbGciOiJSUzI1NiIsImtpZCI6InV4VXlvOXBKcnlCV2dmdlRCcEMzaUNOMHdIalc0cWZlSU50NTB5QjFYR2sifQ.eyJhdWQiOlsiaHR0cHM6Ly9rdWJlcm5ldGVzLmRlZmF1bHQuc3ZjLmNsdXN0ZXIubG9jYWwiLCJrM3MiXSwiZXhwIjoxNzI1MjA1MzkxLCJpYXQiOjE2OTM2NjkzOTEsImlzcyI6Imh0dHBzOi8va3ViZXJuZXRlcy5kZWZhdWx0LnN2Yy5jbHVzdGVyLmxvY2FsIiwia3ViZXJuZXRlcy5pbyI6eyJuYW1lc3BhY2UiOiJrdWJlLWNvbmZpZy1zY2FubmVyIiwicG9kIjp7Im5hbWUiOiJrdWJlLWh1bnRlci1yYzl4ZiIsInVpZCI6IjczOTg5MjQwLWEzNTMtNDUyMS04Nzg0LWY5NDYxOWJiMGZlNCJ9LCJzZXJ2aWNlYWNjb3VudCI6eyJuYW1lIjoiZGVmYXVsdCIsInVpZCI6IjQ0OWFmNjcxLTQxOTMtNDg1ZC05ODUzLWZkMmFhZDc3ZWE4MSJ9LCJ3YXJuYWZ0ZXIiOjE2OTM2NzI5OTh9LCJuYmYiOjE2OTM2NjkzOTEsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLWNvbmZpZy1zY2FubmVyOmRlZmF1bHQifQ.E3KzUZ9F6cuGc3JaIx0Mlsb8tEE8DfyBW9CV4IniCQZcRvX3SCTu7MaDRBacEckHEpahHqjaVBNVUp1JmTSPIGItHky03JWH-TIqxLIeteJG3_Nk3I-vBvoThYJt4fQg5lEVpbnKP0xje0ZWMwN6cvrI7rvXOOMUVw7oNsgOdvtGQQrXfJVrTtsayhiUwoARRAMMwbequM8R9-T6h5G9aoapzniRNr6DLONYS-vMua73cZTE0WBsmhEu9WsThOH9aiioGSHFpTOzuCqkxtG3_pJHF27LrE1v6FUoQ8AML-cgo8x7nOZGRXZ4EG1txzwIGtU9Z5alGi-fcWiuAYt7wA'
      avdReference == 'https://avd.aquasec.com/kube-hunter/khv050/'
      hunter == 'Access Secrets'
    }
  }
}
