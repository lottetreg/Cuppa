package io.github.lottetreg.httpserver.controllers;

import io.github.lottetreg.httpserver.HTTPRequest;
import io.github.lottetreg.httpserver.HTTPResponse;

public class ExampleController extends BaseController {
  public ExampleController(HTTPRequest request) {
    super(request);
  }

  public HTTPResponse empty() {
    return new HTTPResponse.Builder(200).build();
  }

  public HTTPResponse echo() {
    return new HTTPResponse.Builder(200)
        .setBody(this.request.getBody())
        .build();
  }
}
