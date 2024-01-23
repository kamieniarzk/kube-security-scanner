package com.kcs.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public final class MiscUtils {
  private MiscUtils() {}

  /**
   *
   * @return namespace in which the application is deployed (taken from /var/run/secrets or default value of "kube-security-scanner")
   */
  public static String getCurrentNamespace() {
    try {
      return readFileToString("/var/run/secrets/kubernetes.io/serviceaccount/namespace");
    } catch (IOException exception) {
      log.debug("Failed to read current namespace, assuming default");
      return "kss";
    }
  }

  public static String constructServiceAccountName(String helmRelease) {
    return helmRelease.concat("-serviceaccount");
  }

  public static File getFileFromUrl(String stringUrl) throws IOException {
    URL url = new URL(stringUrl);

    File tempFile = File.createTempFile("prefix", "suffix");
    tempFile.deleteOnExit();

    try (InputStream in = url.openStream()) {
      Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    return tempFile;
  }

  private static String readFileToString(String filePath) throws IOException {
    return new String(Files.readAllBytes(Paths.get(filePath)));
  }
}
