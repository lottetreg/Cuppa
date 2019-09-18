package io.github.lottetreg.Cuppa;

public interface Responsive {
  String getPath();

  String getMethod();

  Boolean hasPath(String path);

  Boolean hasMethod(String method);

  Response getResponse(Request request);

  class MissingResource extends RuntimeException {
    MissingResource(String resourcePath, Throwable cause) {
      super(resourcePath, cause);
    }
  }
}
