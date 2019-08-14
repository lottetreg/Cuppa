package io.github.lottetreg.httpserver;

import java.util.HashMap;
import java.util.Map;

public class Response {
  private int statusCode;
  private byte[] body;
  private HashMap<String, String> headers;

  public Response(int statusCode, byte[] body, Map<String, String> headers) {
    this.statusCode = statusCode;
    this.body = body;
    this.headers = new HashMap<>(headers);
  }

  public Response(int statusCode, Map<String, String> headers) {
    this.statusCode = statusCode;
    this.body = new byte[] {};
    this.headers = new HashMap<>(headers);
  }

  public Response(int statusCode, byte[] body) {
    this.statusCode = statusCode;
    this.body = body;
    this.headers = new HashMap<>();
  }

  public Response(int statusCode) {
    this.statusCode = statusCode;
    this.body = new byte[] {};
    this.headers = new HashMap<>();
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public byte[] getBody() {
    return this.body;
  }

  public HashMap<String, String> getHeaders() {
    return this.headers;
  }
}
