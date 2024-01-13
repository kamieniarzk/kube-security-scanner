package com.kcs.apiserver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiServerCommunicationException extends RuntimeException {
  private int httpStatus;
  private String message;

  public ApiServerCommunicationException(Throwable cause) {
    super(cause);
  }
}
