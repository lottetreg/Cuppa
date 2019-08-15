package io.github.lottetreg.httpserver.controllers;

import io.github.lottetreg.httpserver.HTTPRequest;

public class ExampleController extends BaseController {
  public ExampleController(HTTPRequest request) {
    super(request);
  }

  public void empty() {}

  public String echo() {
    return this.request.getBody();
  }

  public byte[] pickles() {
    this.headers.put("Content-Type", getContentType("/pickles.jpg"));
    return readFile("/pickles.jpg");
  }
}
