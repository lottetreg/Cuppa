package io.github.lottetreg.httpserver;

import io.github.lottetreg.httpserver.PositionableStreamReader.StringData;
import io.github.lottetreg.httpserver.PositionableStreamReader.ListData;

public class Reader {
  public HTTPRequest read(Connectionable connection) {
    try {
      PositionableStreamReader reader = new PositionableStreamReader(
              new PositionableInputStream(connection.getInputStream()));

      StringData initialLineData = reader.readFirstLine();
      ListData headersData = reader.readToEmptyLine(initialLineData.endPosition);

      HTTPRequest request = new HTTPRequest(
              new HTTPInitialLine(initialLineData.value),
              new HTTPHeaders(headersData.value));

      int contentLength = request.getContentLength();
      if(contentLength > 0) {
        String body = reader.readNBytes(headersData.endPosition, contentLength).value;
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
