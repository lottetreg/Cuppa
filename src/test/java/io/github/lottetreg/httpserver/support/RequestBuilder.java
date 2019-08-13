package io.github.lottetreg.httpserver.support;

public class RequestBuilder {
  private static String CLRF = "\r\n";

  private String method;
  private String path;
  private String headers;
  private String body;

  public RequestBuilder() {
    this.method = "";
    this.path = "";
    this.headers = "";
    this.body = "";
  }

  public RequestBuilder setMethod(String method) {
    this.method = method;
    return this;
  }

  public RequestBuilder setPath(String path) {
    this.path = path;
    return this;
  }

  public RequestBuilder addHeader(String header) {
    this.headers = this.headers.concat(header + this.CLRF);
    return this;
  }

  public RequestBuilder setBody(String body) {
    this.body = body;
    return this;
  }

  public String build() {
    return this.method + " " + this.path + " HTTP/1.0" + this.CLRF +
        this.headers +
        this.CLRF +
        this.body;
  }
}
