package io.github.lottetreg.httpserver;

import java.io.OutputStream;

public class Writer {
  public void write(Connectionable connection, byte[] output) {
    try {
      OutputStream outputStream = connection.getOutputStream();
      outputStream.write(output);
      outputStream.flush();
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
