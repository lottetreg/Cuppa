package io.github.lottetreg.httpserver;

public interface Routable {
  String getPath();
  String getMethod();
  Boolean hasPath(String path);
  Boolean hasMethod(String method);
  Response getResponse(HTTPRequest request);

  class MissingResource extends RuntimeException {
    MissingResource(String resourcePath, Throwable cause) {
      super(resourcePath, cause);
    }
  } // move into BaseRoute
}
