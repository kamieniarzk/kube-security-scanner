package com.kcs.web;


import com.kcs.shared.CheckSummary;
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
import java.util.Date;
import java.util.List;

@Slf4j
@Component
class CheckSummaryCsvHttpConverter implements GenericHttpMessageConverter<List<CheckSummary>> {
  private static final MediaType CSV = MediaType.valueOf("text/csv");

  @Override
  public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
    return false;
  }

  @Override
  public List<CheckSummary> read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    return null;
  }


  @Override
  public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
    return canWrite(clazz, mediaType) && type != null && type.getTypeName() != null && type.getTypeName().endsWith(String.format("<%s>", CheckSummary.class.getName()));
  }

  @Override
  public void write(List<CheckSummary> checkSummaries, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    write(checkSummaries, contentType, outputMessage);
  }

  @Override
  public boolean canRead(Class<?> clazz, MediaType mediaType) {
    return false;
  }

  @Override
  public boolean canWrite(Class<?> clazz, MediaType mediaType) {
    return CSV.equals(mediaType) && List.class.isAssignableFrom(clazz);
  }

  @Override
  public List<MediaType> getSupportedMediaTypes() {
    return List.of(CSV);
  }

  @Override
  public List<CheckSummary> read(Class<? extends List<CheckSummary>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    return null;
  }

  @Override
  public void write(List<CheckSummary> checkSummaries, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    var headers = outputMessage.getHeaders();
    var fileName = new Date().toString().concat(".csv");
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(fileName));
    headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
    outputMessage.getBody()
        .write(convert(checkSummaries));
  }


  private static byte[] convert(List<CheckSummary> checkSummaries) {
    try (var byteArrayOutputStream = new ByteArrayOutputStream();
         var printWriter = new PrintWriter(byteArrayOutputStream);
         var csvPrinter = new CSVPrinter(printWriter, CSVFormat.DEFAULT)) {

      csvPrinter.printRecord("category", "origin", "originId", "title", "count", "low", "medium", "high", "critical", "unknown");
      for (var check : checkSummaries) {
        csvPrinter.printRecord(check.getCategory(), check.getOrigin(), check.getOriginId(), check.getTitle(), check.getCount(), check.getLowCount(), check.getMediumCount(), check.getHighCount(), check.getCriticalCount(), check.getUnknownCount());
      }

      csvPrinter.flush();
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      log.error("Error while writing workload scan summary to CSV", e);
      throw new RuntimeException("Error while writing workload scan summary to CSV");
    }
  }
}
