package io.github.lottetreg.Cuppa.support;

public class RequestStringBuilder {
  private static String CRLF = "\r\n";

  private String method;
  private String path;
  private String version;
  private String headers;
  private String body;

  public RequestStringBuilder() {
    this.method = "";
    this.path = "";
    this.version = "HTTP/1.0";
    this.headers = "";
    this.body = "";
  }

  public RequestStringBuilder setMethod(String method) {
    this.method = method;
    return this;
  }

  public RequestStringBuilder setPath(String path) {
    this.path = path;
    return this;
  }

  public RequestStringBuilder setVersion(String version) {
    this.version = version;
    return this;
  }

  public RequestStringBuilder addHeader(String header) {
    this.headers = this.headers.concat(header + this.CRLF);
    return this;
  }

  public RequestStringBuilder setBody(String body) {
    this.body = body;
    return this;
  }

  public String build() {
    return this.method + " " + this.path + " " + this.version + this.CRLF +
        this.headers +
        this.CRLF +
        this.body;
  }
}
