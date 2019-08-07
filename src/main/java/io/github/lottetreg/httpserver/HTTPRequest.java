package io.github.lottetreg.httpserver;

public class HTTPRequest {
  private HTTPHeaders headers;
  private HTTPInitialLine initialLine;
  public String body = "";

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
    return this.initialLine.method;
  }

  public String getURI() {
    return this.initialLine.URI;
  }

  public String getHTTPVersion() {
    return this.initialLine.HTTPVersion;
  }

  public String getHeader(String header) {
    return this.headers.getHeader(header);
  }

  public int getContentLength() {
    String contentLength = this.headers.getHeaderOrDefault(
            "Content-Length",
            "0");
    try {
      return Integer.valueOf(contentLength);
    } catch(NumberFormatException e) {
      throw new InvalidContentLengthException(contentLength);
    }
  }

  public void setBody(String body) {
    this.body = body;
  }

  static class InvalidContentLengthException extends RuntimeException {
    InvalidContentLengthException(String contentLength) {
      super("Invalid content length header: " + contentLength);
    }
  }
}
