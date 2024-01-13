package com.kcs.kubebench


import spock.lang.Specification

class KubeBenchFromHumanResultParserTest extends Specification {

  def "should properly parse kube bench result"() {
    given:
    var inputFile = new File("src/test/resources/kube-bench-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    def result = KubeBenchFromHumanResultParser.parse(inputFileString)

    then:
    result.getCategories().size() == 2
  }
}
