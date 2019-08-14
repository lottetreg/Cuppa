package io.github.lottetreg.httpserver;

import java.util.Map;

public class Redirect extends BaseRoute {
  private String redirectPath;

  Redirect(String path, String method, String redirectPath) {
    super(path, method);
    this.redirectPath = redirectPath;
  }

  public Response getResponse(HTTPRequest request) {
    String URI = "http://" + request.getHeader("Host") + getRedirectPath();

    return new Response(301, Map.of("Location", URI));
  }

  public String getRedirectPath() {
    return this.redirectPath;
  }
}
