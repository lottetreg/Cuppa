package io.github.lottetreg.httpserver;

import java.util.Map;

public class HTTPResponse {
  private String CRLF = "\r\n";

  private int statusCode;
  private byte[] body;
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
    return new String(toBytes());
  }

  public byte[] toBytes() {
    byte[] responseData = (initialLine() + joinedHeaders() + this.CRLF).getBytes();
    byte[] responseBody = getBody();

    return concatByteArrays(responseData, responseBody);
  }

  private String initialLine() {
    return getProtocolVersion() + " " + getStatus() + this.CRLF;
  }

  private String getProtocolVersion() {
    return "HTTP/" + this.protocolVersion;
  }

  private String getStatus() {
    return getStatusCode() + " " + this.statuses.get(getStatusCode());
  }

  private String joinedHeaders() {
    Map<String, String> headers = this.headers.getHeaders();

    return headers.keySet().stream()
        .map(key -> key + ": " + headers.get(key))
        .reduce("", (header, acc) -> acc + header + this.CRLF);
  }

  private byte[] getBody() {
    return this.body;
  }

  private byte[] concatByteArrays(byte[] firstArray, byte[] secondArray) {
    byte[] finalArray = new byte[firstArray.length + secondArray.length];

    System.arraycopy(firstArray, 0, finalArray, 0, firstArray.length);
    System.arraycopy(secondArray, 0, finalArray, firstArray.length, secondArray.length);

    return finalArray;
  }

  public static class Builder {
    public int statusCode;
    public byte[] body;
    public String protocolVersion;
    public HTTPHeaders headers;

    public Builder(int statusCode) {
      this.statusCode = statusCode;
      this.body = new byte[] {};
      this.protocolVersion = "1.0";
      this.headers = new HTTPHeaders();
    }

    public Builder setBody(byte[] body) {
      this.body = body;
      return this;
    }

    public Builder setBody(String body) {
      this.body = body.getBytes();
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
