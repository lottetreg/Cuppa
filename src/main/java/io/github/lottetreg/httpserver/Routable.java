package io.github.lottetreg.httpserver;

public interface Routable {
  String getPath();
  String getMethod();
  Boolean hasPath(String path);
  Boolean hasMethod(String method);
  HTTPResponse getResponse(HTTPRequest request);
}
