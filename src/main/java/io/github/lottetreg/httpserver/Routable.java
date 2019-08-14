package io.github.lottetreg.httpserver;

public interface Routable {
  String getPath();
  String getMethod();
  Boolean hasPath(String path);
  Boolean hasMethod(String method);
  Response getResponse(HTTPRequest request);

  class MissingResource extends RuntimeException {
    MissingResource(String resourcePath, Throwable cause) {
      super("Could not find " + resourcePath, cause);
    }
  }

  class FailedToGetResponse extends RuntimeException {
    FailedToGetResponse(String path, String method, Throwable cause) {
      super("Could not get response for " + method + " " + path, cause);
    }
  }
}
