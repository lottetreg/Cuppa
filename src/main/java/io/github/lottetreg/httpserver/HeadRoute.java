package io.github.lottetreg.httpserver;

public class HeadRoute extends BaseRoute {
  HeadRoute(String path) {
    super(path, "HEAD");
  }

  public Response getResponse(HTTPRequest request) {
    return new Response(200);
  }
}
