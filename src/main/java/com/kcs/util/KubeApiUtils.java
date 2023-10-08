package com.kcs.util;

import org.springframework.lang.Nullable;
import org.springframework.util.function.ThrowingSupplier;

import io.kubernetes.client.openapi.ApiException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KubeApiUtils {

  @Nullable
  public static <T> T apiCall(ThrowingSupplier<T> apiCall) {
    try {
      return apiCall.getWithException();
    } catch (Exception exception) {
      if (exception instanceof ApiException apiException) {
        log.warn("ApiException caught, status: {}, body: {}", apiException.getCode(), apiException.getResponseBody());
      }
      throw new RuntimeException(exception);
    }
  }
}
