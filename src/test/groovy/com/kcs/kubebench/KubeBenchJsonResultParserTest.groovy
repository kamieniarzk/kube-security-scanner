package com.kcs.kubebench


import spock.lang.Specification

class KubeBenchJsonResultParserTest extends Specification {

  def "should properly parse kube-bench JSON result"() {
    given:
    var inputFile = new File("src/test/resources/kube-bench-json-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    var result = KubeBenchJsonResultParser.parse(inputFileString)

    then: 'controls are parsed'

    result.controls.size() == 2

    and: 'totals are parsed'
    with(result.totals) {
      totalPass == 2
      totalFail == 9
      totalWarn == 47
      totalInfo == 0
    }
  }
}
