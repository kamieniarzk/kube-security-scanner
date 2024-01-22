package com.kcs.shareed

import com.kcs.shared.Check
import com.kcs.shared.KubernetesResource
import com.kcs.shared.ResultSearchParams
import com.kcs.shared.ScanResult
import com.kcs.shared.ScanResultFilter
import com.kcs.shared.Severity
import spock.lang.Specification

class ScanResultFilterSpec extends Specification {

  def "filter returns all results when no search parameters are set"() {
    given: "A ScanResultFilter with no search parameters"
    def searchParams = ResultSearchParams.empty()
    def filter = ScanResultFilter.withParams(searchParams)

    and: "A sample ScanResult"
    def namespacedResources = ['test-namespace' : [], 'default' : [], 'kube-system' : []]
    def nonNamespacedResources = [new KubernetesResource('kind', 'namespace', 'name', [new Check(Severity.CRITICAL, 'title', 'description', 'remediation', 'origin', 'originId')])]
    def sampleResult = new ScanResult(namespacedResources, nonNamespacedResources, null)

    when: "Filter is applied"
    def filteredResult = filter.filter(sampleResult)

    then: "All results are returned"
    with(filteredResult) {
      namespacedResources == sampleResult.namespacedResources
      nonNamespacedResources == sampleResult.nonNamespacedResources
      skippedChecks == sampleResult.skippedChecks
      scanId == sampleResult.scanId
      aggregated == sampleResult.aggregated
    }
  }

  def "filter applies namespace filtering correctly"() {
    given: "A ScanResultFilter with specific namespace search parameter"
    def searchParams = new ResultSearchParams(Set.of('test-namespace'), null, null, null, null, null)
    def filter = ScanResultFilter.withParams(searchParams)

    and: "A sample ScanResult with multiple namespaces"
    def namespacedResources = ['test-namespace' : [], 'default' : [], 'kube-system' : []]
    def nonNamespacedResources = [new KubernetesResource('kind', 'namespace', 'name', [new Check(Severity.CRITICAL, 'title', 'description', 'remediation', 'origin', 'originId')])]
    def sampleResult = new ScanResult(namespacedResources, nonNamespacedResources, null)

    when: "Filter is applied"
    def filteredResult = filter.filter(sampleResult)

    then: "Only resources from 'test-namespace' are returned"
    filteredResult.namespacedResources.keySet() == Set.of('test-namespace')
  }

  def "filter applies resource-level filters correctly"() {
    given: "A ScanResultFilter with specific resource search parameters"
    def searchParams = new ResultSearchParams(null, Set.of('test-kind'), Set.of('test-name'), null, null, null)
    def filter = ScanResultFilter.withParams(searchParams)

    and: "A sampleScanResult with various resources"
    def resource1 = new KubernetesResource('test-kind', null,'test-name', null)
    def resource2 = new KubernetesResource('test-kind', null, 'other-name', null)
    def resource3 = new KubernetesResource('other-kind', null, 'test-name', null)
    def namespacedResources = [
        'namespace1': [resource1, resource2],
        'namespace2': [resource3]
    ]
    def nonNamespacedResources = [resource2, resource3]
    def sampleResult = new ScanResult(namespacedResources, nonNamespacedResources, null)

    when: "Filter is applied"
    def filteredResult = filter.filter(sampleResult)

    then: "Only resources with 'test-name' and 'test-kind' are returned"
    filteredResult.namespacedResources.every { _, resources ->
      resources.every { it.name == 'test-name' && it.kind == 'test-kind' }
    }
    filteredResult.nonNamespacedResources.every { it.name == 'test-name' && it.kind == 'test-kind' }
  }

  def "filter handles empty result set correctly"() {
    given: "A ScanResultFilter with specific search parameters that match no resources"
    def searchParams = new ResultSearchParams(Set.of('non-existent'), Set.of('non-existent'), null, null, null, null)
    def filter = ScanResultFilter.withParams(searchParams)

    and: "A sample ScanResult with resources that do not match the filter"
    def namespacedResources = ['test-namespace' : [], 'default' : [], 'kube-system' : []]
    def nonNamespacedResources = [new KubernetesResource('kind', 'namespace', 'name', [new Check(Severity.CRITICAL, 'title', 'description', 'remediation', 'origin', 'originId')])]
    def sampleResult = new ScanResult(namespacedResources, nonNamespacedResources, null)

    when: "Filter is applied"
    def filteredResult = filter.filter(sampleResult)

    then: "No resources are returned"
    filteredResult.namespacedResources.values().every { it.isEmpty() }
    filteredResult.nonNamespacedResources.isEmpty()
  }
}
