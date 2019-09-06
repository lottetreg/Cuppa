package io.github.lottetreg.httpserver;

import java.util.HashMap;
import java.util.Map;

public class HTTPResponse {
  private String CRLF = "\r\n";

  private int statusCode;
  private HashMap<String, String> headers;
  private byte[] body;
  private String protocolVersion;

  private Map<Integer, String> statuses = Map.of(
      200, "OK",
      301, "Moved Permanently",
      400, "Bad Request",
      404, "Not Found",
      405, "Method Not Allowed",
      500, "Internal Server Error"
  );

  HTTPResponse(Builder builder) {
    this.statusCode = builder.statusCode;
    this.headers = builder.headers;
    this.body = builder.body;
    this.protocolVersion = builder.protocolVersion;
  }

  public byte[] toBytes() {
    byte[] responseData = (initialLine() + joinedHeaders() + this.CRLF).getBytes();
    return concatByteArrays(responseData, this.body);
  }

  private String initialLine() {
    return getProtocolVersion() + " " + getStatus() + this.CRLF;
  }

  private String getProtocolVersion() {
    return "HTTP/" + this.protocolVersion;
  }

  private String getStatus() {
    return this.statusCode + " " + this.statuses.get(this.statusCode);
  }

  private String joinedHeaders() {
    return this.headers.keySet().stream()
        .map(key -> key + ": " + headers.get(key) + this.CRLF)
        .reduce("", (header, acc) -> acc + header);
  }

  private byte[] concatByteArrays(byte[] firstArray, byte[] secondArray) {
    byte[] finalArray = new byte[firstArray.length + secondArray.length];

    System.arraycopy(firstArray, 0, finalArray, 0, firstArray.length);
    System.arraycopy(secondArray, 0, finalArray, firstArray.length, secondArray.length);

    return finalArray;
  }

  public static class Builder {
    public int statusCode;
    public HashMap<String, String> headers;
    public byte[] body;
    public String protocolVersion;

    public Builder(int statusCode) {
      this.statusCode = statusCode;
      this.headers = new HashMap<>();
      this.body = new byte[] {};
      this.protocolVersion = "1.0";
    }

    public Builder setBody(byte[] body) {
      this.body = body;
      return this;
    }

    public Builder setHeaders(HashMap headers) {
      this.headers = headers;
      return this;
    }

    public HTTPResponse build() {
      return new HTTPResponse(this);
    }
  }
}
