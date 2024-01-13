package com.kcs.web;

import com.kcs.compliance.ComplianceByNamespaceSummary;
import com.kcs.compliance.ComplianceSummary;
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
class ComplianceByNamespaceSummaryCsvHttpConverter implements GenericHttpMessageConverter<ComplianceByNamespaceSummary> {

  private static final MediaType CSV = MediaType.valueOf("text/csv");

  @Override
  public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
    return false;
  }

  @Override
  public ComplianceByNamespaceSummary read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
    return canWrite(clazz, mediaType) && type.getTypeName() != null && (type.getTypeName().equals(ComplianceByNamespaceSummary.class.getTypeName()) || type.getTypeName().endsWith(String.format("<%s>", ComplianceByNamespaceSummary.class.getName())));
  }

  @Override
  public void write(ComplianceByNamespaceSummary complianceByNamespaceSummary, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    write(complianceByNamespaceSummary, contentType, outputMessage);
  }

  @Override
  public boolean canRead(Class<?> clazz, MediaType mediaType) {
    return false;
  }

  @Override
  public boolean canWrite(Class<?> clazz, MediaType mediaType) {
    return CSV.equals(mediaType) && ComplianceByNamespaceSummary.class.isAssignableFrom(clazz);
  }

  @Override
  public List<MediaType> getSupportedMediaTypes() {
    return List.of(CSV);
  }

  @Override
  public ComplianceByNamespaceSummary read(Class<? extends ComplianceByNamespaceSummary> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void write(ComplianceByNamespaceSummary complianceByNamespaceSummary, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    var headers = outputMessage.getHeaders();
    var framework = complianceByNamespaceSummary.framework();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s-compliance.csv".formatted(framework));
    headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
    outputMessage.getBody()
        .write(convert(complianceByNamespaceSummary));
  }

  private static byte[] convert(ComplianceByNamespaceSummary summary) {
    try (var byteArrayOutputStream = new ByteArrayOutputStream();
         var printWriter = new PrintWriter(byteArrayOutputStream);
         var csvPrinter = new CSVPrinter(printWriter, CSVFormat.DEFAULT)) {

      csvPrinter.printRecord("namespace", "failedChecks", "passedChecks", "failedResources", "passedResources", "score");

      for (var entry : summary.summaryMap().entrySet()) {
        var namespace = entry.getKey();
        var compliance = entry.getValue();
        csvPrinter.printRecord(namespace, compliance.failedChecks(), compliance.passedChecks(), compliance.failedResources(), compliance.passedResources(), compliance.score());
      }

      var global = summary.globalSummary();
      csvPrinter.printRecord(null, global.failedChecks(), global.passedChecks(), global.failedResources(), global.passedResources(), global.score());

      csvPrinter.flush();
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      log.error("Error while writing compliance summary to CSV", e);
      throw new RuntimeException("Error while writing compliance summary to CSV");
    }
  }
}
