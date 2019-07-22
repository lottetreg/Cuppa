package io.github.lottetreg.httpserver;

import java.io.OutputStreamWriter;

public class Writer {
  public void write(Connectionable connection, String output) {
    try {
      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
      writer.write(output, 0, output.length());
      writer.flush();
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
