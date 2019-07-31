package io.github.lottetreg.httpserver;

import java.util.List;

public class Reader {
  public HTTPRequest read(Connectionable connection) {
    try {
      StreamReader streamReader =
          new StreamReader(connection.getInputStream());

      String initialLine = streamReader.readLine();
      List<String> headerLines = streamReader.readLinesUntilEmptyLine();

      HTTPRequest request = new HTTPRequest(
          new HTTPInitialLine(initialLine),
          new HTTPHeaders(headerLines));

      String body = streamReader.readNChars(request.getContentLength());
      request.setBody(body);

      return request;
    } catch (Connection.FailedToGetInputStream e) {
      throw e;
    } catch (Exception e) {
      throw new FailedToReadFromConnection(e);
    }
  }

  static class FailedToReadFromConnection extends RuntimeException {
    FailedToReadFromConnection(Throwable cause) {
      super("Failed to read from the connection", cause);
    }
  }
}
