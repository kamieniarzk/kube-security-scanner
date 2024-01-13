package com.kcs.web;

import com.google.protobuf.Api;
import com.kcs.apiserver.ApiServerCommunicationException;
import com.kcs.shared.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class WebExceptionHandler {

  @ExceptionHandler({NoDataFoundException.class})
  ResponseEntity<RestError> handleNoDataFound(NoDataFoundException noDataFoundException) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestError(noDataFoundException.getMessage(), HttpStatus.NOT_FOUND));
  }

  @ExceptionHandler({ApiServerCommunicationException.class})
  ResponseEntity<RestError> handleApiServerCommunicationException(ApiServerCommunicationException exception) {
    var message = "Api server communication error occurred, status %s, response body: %s".formatted(exception.getHttpStatus(), exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestError(message, HttpStatus.FAILED_DEPENDENCY));
  }
}
