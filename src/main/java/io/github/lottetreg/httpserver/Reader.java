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

      int contentLength = request.getContentLength();
      if(contentLength > 0) {
        String body = streamReader.readNChars(contentLength);
        request.setBody(body);
      }

      return request;
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
