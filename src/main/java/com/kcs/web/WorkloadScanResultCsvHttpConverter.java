package com.kcs.web;


import com.kcs.workload.K8sResource;
import com.kcs.workload.Severity;
import com.kcs.workload.WorkloadScanResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@Component
class WorkloadScanResultCsvHttpConverter implements GenericHttpMessageConverter<WorkloadScanResult> {
  private static final MediaType CSV = MediaType.valueOf("text/csv");

  @Override
  public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
    return false;
  }

  @Override
  public WorkloadScanResult read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
    return canWrite(clazz, mediaType) && type.getTypeName() != null && (type.getTypeName().equals(WorkloadScanResult.class.getTypeName()) || type.getTypeName().endsWith(String.format("<%s>", WorkloadScanResult.class.getName())));
  }

  @Override
  public void write(WorkloadScanResult workloadScanResult, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    write(workloadScanResult, contentType, outputMessage);
  }

  @Override
  public boolean canRead(Class<?> clazz, MediaType mediaType) {
    return false;
  }

  @Override
  public boolean canWrite(Class<?> clazz, MediaType mediaType) {
    return CSV.equals(mediaType) && WorkloadScanResult.class.isAssignableFrom(clazz);
  }

  @Override
  public List<MediaType> getSupportedMediaTypes() {
    return List.of(CSV);
  }

  @Override
  public WorkloadScanResult read(Class<? extends WorkloadScanResult> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void write(WorkloadScanResult workloadScanResult, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    var headers = outputMessage.getHeaders();
    var aggregated = workloadScanResult.getAggregated();
    var fileName = aggregated ? "aggregated-".concat(workloadScanResult.getScanId()) : workloadScanResult.getScanId();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s.csv".formatted(fileName));
    headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
    outputMessage.getBody()
        .write(convert(workloadScanResult));
  }

  private static byte[] convert(WorkloadScanResult workloadScanResult) {
    try (var byteArrayOutputStream = new ByteArrayOutputStream();
         var printWriter = new PrintWriter(byteArrayOutputStream);
         var csvPrinter = new CSVPrinter(printWriter, CSVFormat.DEFAULT)) {

      csvPrinter.printRecord("namespace", "kind", "name", "low", "medium", "high", "critical");
      for (var entry : workloadScanResult.getNamespacedResources().entrySet()) {
        var namespace = entry.getKey();
        printAllResourcesInNamespace(entry.getValue(), csvPrinter, namespace);
      }
      printAllResourcesInNamespace(workloadScanResult.getNonNamespacedResources(), csvPrinter, null);
      csvPrinter.flush();
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      log.error("Error while writing workload scan summary to CSV", e);
      throw new RuntimeException("Error while writing workload scan summary to CSV");
    }
  }

  private static void printAllResourcesInNamespace(List<K8sResource> resources, CSVPrinter csvPrinter, String namespace) throws IOException {
    for (var resource : resources) {
      var low = countChecksWithSeverity(resource, Severity.LOW);
      var medium = countChecksWithSeverity(resource, Severity.MEDIUM);
      var high = countChecksWithSeverity(resource, Severity.HIGH);
      var critical = countChecksWithSeverity(resource, Severity.CRITICAL);
      csvPrinter.printRecord(namespace, resource.getKind(), resource.getName(), low, medium, high, critical);
    }
  }

  private static int countChecksWithSeverity(K8sResource resource, Severity severity) {
    return (int) resource.getChecks().stream().filter(check -> severity.equals(check.severity())).count();
  }
}
