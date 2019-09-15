package io.github.lottetreg.Cuppa;

import java.io.IOException;
import java.util.HashMap;

public class HTTPRequest {
  private Outable out;
  private String method;
  private String path;
  private String protocolVersion;
  private HashMap<String, String> headers;
  private String body;

  HTTPRequest(ParsedRequest parsedRequest) throws IOException {
    this.out = new Out();
    setFieldsFromParsedRequest(parsedRequest);
  }

  HTTPRequest(ParsedRequest parsedRequest, Outable out) throws IOException {
    this.out = out;
    setFieldsFromParsedRequest(parsedRequest);
  }

  private void setFieldsFromParsedRequest(ParsedRequest parsedRequest) throws IOException {
    this.method = parsedRequest.getMethod();
    this.path = parsedRequest.getPath();
    this.protocolVersion = parsedRequest.getVersion();
    this.headers = parsedRequest.getHeaders();
    this.body = parsedRequest.getStreamReader().readNChars(getContentLength());
  }

  public String getMethod() {
    return this.method;
  }

  public String getPath() {
    return this.path;
  }

  public String getProtocolVersion() {
    return this.protocolVersion;
  }

  public String getHeader(String headerName) {
    return this.headers.get(headerName);
  }

  public String getBody() {
    return this.body;
  }

  public int getContentLength() {
    String contentLength = this.headers.getOrDefault(
        "Content-Length",
        "0");
    try {
      return Integer.valueOf(contentLength);
    } catch (NumberFormatException e) {
      this.out.println("Invalid Content-Length: " + contentLength);
      return 0;
    }
  }
}
