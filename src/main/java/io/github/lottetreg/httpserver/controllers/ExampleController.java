package io.github.lottetreg.httpserver.controllers;

import io.github.lottetreg.httpserver.HTTPRequest;
import io.github.lottetreg.httpserver.Response;

import java.util.Map;

public class ExampleController extends BaseController {
  public ExampleController(HTTPRequest request) {
    super(request);
  }

  public Response empty() {
    return new Response(200);
  }

  public Response echo() {
    return new Response(200, this.request.getBody().getBytes());
  }

  public Response pickles() {
    String imagePath = "/pickles.jpg";

    return new Response(200, readFile(imagePath), Map.of("Content-Type", getContentType(imagePath)));
  }
}
