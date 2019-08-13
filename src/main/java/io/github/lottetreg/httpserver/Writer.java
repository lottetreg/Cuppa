package io.github.lottetreg.httpserver;

import java.io.OutputStreamWriter;

public class Writer {
  public void write(Connectionable connection, String output) {
    try {
      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
      writer.write(output, 0, output.length());
      writer.flush();
    } catch (Connection.FailedToGetOutputStream e) {
      throw e;
    } catch (Exception e) {
      throw new FailedToWriteToConnection(e);
    }
  }

  static class FailedToWriteToConnection extends RuntimeException {
    FailedToWriteToConnection(Throwable cause) {
      super("Failed to write to the connection", cause);
    }
  }
}
