package com.github.lottetreg.cup;

public interface Responsive {
  String getPath();

  String getMethod();

  Response getResponse(Request request);

  class MissingResource extends RuntimeException {
    MissingResource(String resourcePath, Throwable cause) {
      super(resourcePath, cause);
    }
  }
}
