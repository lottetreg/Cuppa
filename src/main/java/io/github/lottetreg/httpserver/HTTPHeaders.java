package io.github.lottetreg.httpserver;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class HTTPHeaders {
  private Map<String, String> headers;
  private String headerSeparator = ": ";

  HTTPHeaders(List<String> headersList) {
    this.headers = headersList.stream().collect(toMap(
            header -> getHeaderName(header),
            header -> getHeaderValue(header)));
  }

  public String getHeader(String headerName) {
    return this.headers.get(headerName);
  }

  public String getHeaderOrDefault(String headerName, String defaultValue) {
    return this.headers.getOrDefault(headerName, defaultValue);
  }

  private String getHeaderName(String header) {
    String headerName = splitHeader(header)[0];

    if(headerName.isEmpty()) {
      throw new IncorrectlyFormattedHeaderException(header);
    }

    return headerName;
  }

  private String getHeaderValue(String header) {
    String headerValue = splitHeader(header)[1];

    if(headerValue.isEmpty()) {
      throw new IncorrectlyFormattedHeaderException(header);
    }

    return headerValue;
  }

  private String[] splitHeader(String header) {
    String[] splitHeader = header.split(headerSeparator, 2);

    if(splitHeader.length != 2) {
      throw new IncorrectlyFormattedHeaderException(header);
    }

    return splitHeader;
  }

  static class IncorrectlyFormattedHeaderException extends RuntimeException {
    IncorrectlyFormattedHeaderException(String header) {
      super("Header is incorrectly formatted: " + header);
    }
  }
}
