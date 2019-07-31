package io.github.lottetreg.httpserver;

import java.util.Map;

public class HTTPResponse {
  private static String CRLF = "\r\n";

  private int statusCode;
  private String body;
  private String protocolVersion;
  private HTTPHeaders headers;

  private Map<Integer, String> statuses = Map.of(
      200, "OK",
      301, "Moved Permanently",
      404, "Not Found",
      405, "Method Not Allowed",
      500, "Internal Server Error"
  );

  HTTPResponse(Builder builder) {
    this.statusCode = builder.statusCode;
    this.body = builder.body;
    this.protocolVersion = builder.protocolVersion;
    this.headers = builder.headers;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public HTTPHeaders getHeaders() {
    return this.headers;
  }

  public HTTPResponse addHeader(String headerName, String headerValue) {
    this.headers.addHeader(headerName, headerValue);
    return this;
  }

  public String toString() {
    return getProtocolVersion() + " " + getStatus() + this.CRLF + joinedHeaders() + this.CRLF + getBody();
  }

  private String joinedHeaders() {
    Map<String, String> headers = this.headers.getHeaders();

    return headers.keySet().stream()
        .map(key -> key + ": " + headers.get(key))
        .reduce("", (header, acc) -> acc + header + this.CRLF);
  }

  private String getProtocolVersion() {
    String protocol = "HTTP";
    return protocol + "/" + this.protocolVersion;
  }

  private String getStatus() {
    int statusCode = getStatusCode();
    return statusCode + " " + this.statuses.get(statusCode);
  }

  private String getBody() {
    return this.body;
  }

  public static class Builder {
    public int statusCode;
    public String body;
    public String protocolVersion;
    public HTTPHeaders headers;

    public Builder(int statusCode) {
      this.statusCode = statusCode;
      this.body = "";
      this.protocolVersion = "1.0";
      this.headers = new HTTPHeaders();
    }

    public Builder setBody(String body) {
      this.body = body;
      return this;
    }

    public Builder setProtocolVersion(String protocolVersion) {
      this.protocolVersion = protocolVersion;
      return this;
    }

    public Builder setHeaders(HTTPHeaders headers) {
      this.headers = headers;
      return this;
    }

    public HTTPResponse build() {
      return new HTTPResponse(this);
    }
  }
}
