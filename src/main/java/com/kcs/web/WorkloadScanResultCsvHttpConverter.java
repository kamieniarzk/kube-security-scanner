package com.kcs.web;


import com.kcs.shared.KubernetesResource;
import com.kcs.shared.Severity;
import com.kcs.shared.ScanResult;
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
class WorkloadScanResultCsvHttpConverter implements GenericHttpMessageConverter<ScanResult> {
  private static final MediaType CSV = MediaType.valueOf("text/csv");

  @Override
  public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
    return false;
  }

  @Override
  public ScanResult read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
    return canWrite(clazz, mediaType) && type.getTypeName() != null && (type.getTypeName().equals(ScanResult.class.getTypeName()) || type.getTypeName().endsWith(String.format("<%s>", ScanResult.class.getName())));
  }

  @Override
  public void write(ScanResult scanResult, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    write(scanResult, contentType, outputMessage);
  }

  @Override
  public boolean canRead(Class<?> clazz, MediaType mediaType) {
    return false;
  }

  @Override
  public boolean canWrite(Class<?> clazz, MediaType mediaType) {
    return CSV.equals(mediaType) && ScanResult.class.isAssignableFrom(clazz);
  }

  @Override
  public List<MediaType> getSupportedMediaTypes() {
    return List.of(CSV);
  }

  @Override
  public ScanResult read(Class<? extends ScanResult> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void write(ScanResult scanResult, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    var headers = outputMessage.getHeaders();
    var aggregated = scanResult.getAggregated();
    var fileName = aggregated ? "aggregated-".concat(scanResult.getScanId()) : scanResult.getScanId();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s.csv".formatted(fileName));
    headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
    outputMessage.getBody()
        .write(convert(scanResult));
  }

  private static byte[] convert(ScanResult scanResult) {
    try (var byteArrayOutputStream = new ByteArrayOutputStream();
         var printWriter = new PrintWriter(byteArrayOutputStream);
         var csvPrinter = new CSVPrinter(printWriter, CSVFormat.DEFAULT)) {

      csvPrinter.printRecord("namespace", "kind", "name", "low", "medium", "high", "critical", "unknown");
      for (var entry : scanResult.getNamespacedResources().entrySet()) {
        var namespace = entry.getKey();
        printAllResourcesInNamespace(entry.getValue(), csvPrinter, namespace);
      }
      printAllResourcesInNamespace(scanResult.getNonNamespacedResources(), csvPrinter, null);
      csvPrinter.flush();
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      log.error("Error while writing workload scan summary to CSV", e);
      throw new RuntimeException("Error while writing workload scan summary to CSV");
    }
  }

  private static void printAllResourcesInNamespace(List<KubernetesResource> resources, CSVPrinter csvPrinter, String namespace) throws IOException {
    for (var resource : resources) {
      var low = countChecksWithSeverity(resource, Severity.LOW);
      var medium = countChecksWithSeverity(resource, Severity.MEDIUM);
      var high = countChecksWithSeverity(resource, Severity.HIGH);
      var critical = countChecksWithSeverity(resource, Severity.CRITICAL);
      var kubeScore = countChecksWithSeverity(resource, Severity.UNKNOWN);
      csvPrinter.printRecord(namespace, resource.getKind(), resource.getName(), low, medium, high, critical, kubeScore);
    }
  }

  private static int countChecksWithSeverity(KubernetesResource resource, Severity severity) {
    return (int) resource.getChecks().stream().filter(check -> severity.equals(check.severity())).count();
  }
}
