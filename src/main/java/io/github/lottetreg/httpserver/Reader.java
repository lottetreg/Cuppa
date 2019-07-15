package io.github.lottetreg.httpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Reader {
  public String read(Connectionable connection) {
    try {
      InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      return bufferedReader.readLine();
    } catch (Connection.FailedToGetInputStreamException e) {
      throw e;
    } catch (Exception e) {
      throw new FailedToReadFromConnectionException(e);
    }
  }

  static class FailedToReadFromConnectionException extends RuntimeException {
    FailedToReadFromConnectionException(Throwable cause) {
      super("Failed to read from the connection", cause);
    }
  }
}
