package io.github.lottetreg.httpserver;

import java.util.Map;

public class OptionsRoute extends BaseRoute {
  OptionsRoute(String path) {
    super(path, "OPTIONS");
  }

  public Response getResponse(HTTPRequest request) {
    return new Response(200, Map.of("Allow", getAllowedMethods()));
  }
}
