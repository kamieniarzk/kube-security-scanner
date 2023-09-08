package com.kcs.bench


import spock.lang.Specification

class KubeBenchResultParserTest extends Specification {

  def "should properly parse kube bench result"() {
    given:
    var inputFile = new File("src/test/resources/kube-bench-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    def result = KubeBenchResultParser.parse(inputFileString)

    then:
    result.getCategories().size() == 2
  }
}
