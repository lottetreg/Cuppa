package io.github.lottetreg.httpserver;

import java.util.HashMap;
import java.util.Map;

public class Response {
  private int statusCode;
  private byte[] body = new byte[] {};
  private HashMap<String, String> headers = new HashMap<>();

  public Response(int statusCode, byte[] body, Map<String, String> headers) {
    this.statusCode = statusCode;
    this.body = body;
    this.headers = new HashMap<>(headers);
  }

  public Response(int statusCode, byte[] body) {
    this.statusCode = statusCode;
    this.body = body;
  }

  public Response(int statusCode, Map<String, String> headers) {
    this.statusCode = statusCode;
    this.headers = new HashMap<>(headers);
  }

  public Response(int statusCode) {
    this.statusCode = statusCode;
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
