package com.github.lottetreg.cup;

import java.util.HashMap;
import java.util.Map;

public class Response {
  private int statusCode;
  private HashMap<String, String> headers = new HashMap<>();
  private byte[] body = new byte[] {};

  public Response(int statusCode, Map<String, String> headers, byte[] body) {
    this.statusCode = statusCode;
    this.headers = new HashMap<>(headers);
    this.body = body;
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

  public HashMap<String, String> getHeaders() {
    return this.headers;
  }

  public byte[] getBody() {
    return this.body;
  }
}
