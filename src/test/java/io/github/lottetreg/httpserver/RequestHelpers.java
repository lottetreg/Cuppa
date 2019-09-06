package io.github.lottetreg.httpserver;

import io.github.lottetreg.httpserver.support.RequestStringBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

class RequestHelpers {
  static HTTPRequest buildHTTPRequest(String method, String path) throws IOException {
    String request = new RequestStringBuilder()
        .setMethod(method)
        .setPath(path)
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());
    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    return new HTTPRequest(parsedRequest);
  }

  static HTTPRequest buildHTTPRequest(String method, String path, List<String> headers) throws IOException {
    RequestStringBuilder requestBuilder = new RequestStringBuilder()
        .setMethod(method)
        .setPath(path);

    headers.forEach(header -> {
      requestBuilder.addHeader(header);
    });

    String request = requestBuilder.build();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());
    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    return new HTTPRequest(parsedRequest);
  }
}
