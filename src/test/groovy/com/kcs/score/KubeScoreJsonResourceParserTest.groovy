package com.kcs.score

import spock.lang.Specification

class KubeScoreJsonResourceParserTest extends Specification {

  def "should properly parse json format result"() {
    given:
    var inputFile = new File("src/test/resources/kube-score-json-example")
    var inputFileString = new String(inputFile.readBytes())

    when:
    var result = KubeScoreJsonResourceParser.parseFullResult(inputFileString)

    then:
    result.size() > 0
  }
}
