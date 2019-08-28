package io.github.lottetreg.httpserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class HTTPHeaders {
  private HashMap<String, String> headers;
  private String headerSeparator = ": ";

  public HTTPHeaders(List<String> headersList) {
    this.headers = new HashMap<>(headersList.stream().collect(toMap(
        header -> getHeaderName(header),
        header -> getHeaderValue(header))));
  }

  public HTTPHeaders(Map<String, String> headers) {
    this.headers = new HashMap<>(headers);
  }

  public HTTPHeaders() {
    this.headers = new HashMap<>();
  }

  public HashMap<String, String> getHeaders() {
    return this.headers;
  }

  public String getHeader(String headerName) {
    return this.headers.get(headerName);
  }

  public String getHeader(String headerName, String defaultValue) {
    return this.headers.getOrDefault(headerName, defaultValue);
  }

  public void addHeader(String headerName, String headerValue) {
    this.headers.put(headerName, headerValue);
  }

  private String getHeaderName(String header) {
    String headerName = splitHeader(header)[0];

    if (headerName.isEmpty()) {
      throw new IncorrectlyFormattedHeader(header);
    }

    return headerName;
  }

  private String getHeaderValue(String header) {
    String headerValue = splitHeader(header)[1];

    if (headerValue.isEmpty()) {
      throw new IncorrectlyFormattedHeader(header);
    }

    return headerValue;
  }

  private String[] splitHeader(String header) {
    String[] splitHeader = header.split(headerSeparator, 2);

    if (splitHeader.length != 2) {
      throw new IncorrectlyFormattedHeader(header);
    }

    return splitHeader;
  }

  static class IncorrectlyFormattedHeader extends RuntimeException {
    IncorrectlyFormattedHeader(String header) {
      super("Header is incorrectly formatted: " + header);
    }
  }
}
