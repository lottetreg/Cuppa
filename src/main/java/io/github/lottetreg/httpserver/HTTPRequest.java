package io.github.lottetreg.httpserver;

public class HTTPRequest {
  private HTTPHeaders headers;
  private HTTPInitialLine initialLine;
  private String body = "";

  HTTPRequest(HTTPInitialLine initialLine) {
    this.initialLine = initialLine;
    this.headers = new HTTPHeaders();
  }

  HTTPRequest(HTTPInitialLine initialLine, HTTPHeaders headers) {
    this.initialLine = initialLine;
    this.headers = headers;
  }

  HTTPRequest(HTTPInitialLine initialLine, HTTPHeaders headers, String body) {
    this.initialLine = initialLine;
    this.headers = headers;
    this.body = body;
  }

  public String getMethod() {
    return this.initialLine.getMethod();
  }

  public String getURI() {
    return this.initialLine.getURI();
  }

  public String getHTTPVersion() {
    return this.initialLine.getHTTPVersion();
  }

  public String getHeader(String header) {
    return this.headers.getHeader(header);
  }

  public int getContentLength() {
    String contentLength = this.headers.getHeader(
            "Content-Length",
            "0");
    try {
      return Integer.valueOf(contentLength);
    } catch (NumberFormatException e) {
      throw new InvalidContentLength(contentLength); // log error but still return 0?
    }
  }

  public String getBody() {
    return this.body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  static class InvalidContentLength extends RuntimeException {
    InvalidContentLength(String contentLength) {
      super("Invalid content length header: " + contentLength);
    }
  }
}
