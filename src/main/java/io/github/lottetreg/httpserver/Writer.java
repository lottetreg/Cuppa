package io.github.lottetreg.httpserver;

import java.io.PrintWriter;

public class Writer {
  public void write(Connectionable connection, String output) {
    try {
      PrintWriter printWriter = new PrintWriter(connection.getOutputStream(), true);
      printWriter.println(output);
    } catch (Connection.FailedToGetOutputStreamException e) {
      throw e;
    } catch (Exception e) {
      throw new FailedToWriteToConnectionException(e);
    }
  }

  static class FailedToWriteToConnectionException extends RuntimeException {
    FailedToWriteToConnectionException(Throwable cause) {
      super("Failed to write to the connection", cause);
    }
  }
}
