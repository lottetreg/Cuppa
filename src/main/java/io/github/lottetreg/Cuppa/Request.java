package io.github.lottetreg.Cuppa;

import java.util.HashMap;

class Request {
  private String method;
  private String path;
  private HashMap<String, String> headers;
  private String body;

  Request(String method, String path, HashMap<String, String> headers, String body) {
    this.method = method;
    this.path = path;
    this.headers = headers;
    this.body = body;
  }

  String getMethod() {
    return this.method;
  }

  String getPath() {
    return this.path;
  }

  HashMap<String, String> getHeaders() {
    return this.headers;
  }

  String getHeader(String headerName) {
    return getHeaders().get(headerName);
  }

  String getBody() {
    return this.body;
  }
}
