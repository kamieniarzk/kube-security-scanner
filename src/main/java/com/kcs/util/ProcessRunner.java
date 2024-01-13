package com.kcs.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class ProcessRunner {

  public static String runWithExceptionHandling(String command) {
    try {
      var runResult = ProcessRunner.run(command);
      if (runResult.exitCode() != 0) {
        var message = "Process run error - stderr:\n%s".formatted(runResult.stdErr);
        log.error(message);
        throw new RuntimeException();
      }
      return runResult.stdIn();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static RunResult run(String command) throws IOException, InterruptedException {
    var builder = new ProcessBuilder();
    var commandSeparated = command.split(" ");
    builder.command(commandSeparated);
    var process = builder.start();
    var streamConsumer = new StreamConsumer(process.getInputStream(), process.getErrorStream());
    streamConsumer.consume();
    var exitCode = process.waitFor();
    return new RunResult(streamConsumer.stdIn.toString(), streamConsumer.stdErr.toString(), exitCode);
  }

  @AllArgsConstructor
  private static class StreamConsumer {
    @Getter private final StringBuilder stdIn = new StringBuilder();
    @Getter private final StringBuilder stdErr = new StringBuilder();
    private InputStream inputStream;
    private InputStream errorStream;

    public void consume() {
      new BufferedReader(new InputStreamReader(inputStream)).lines()
          .forEach(line -> stdIn.append(line).append('\n'));

      new BufferedReader(new InputStreamReader(errorStream)).lines()
          .forEach(line -> stdErr.append(line).append('\n'));
    }
  }

  public record RunResult(String stdIn, String stdErr, int exitCode) { }
}
