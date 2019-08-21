package io.github.lottetreg.httpserver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface Routable {
  String getPath();

  String getMethod();

  Boolean hasPath(String path);

  Boolean hasMethod(String method);

  String getAllowedMethods();

  Response getResponse(HTTPRequest request);

  ArrayList<Routable> routables = new ArrayList<>();

  static void store(Routable routable) {
    routables.add(routable);
  }

  static void clearStoredRoutables() {
    routables.clear();
  }

  static String getAllowedMethods(String path) {
    return routables.stream()
        .filter(routable -> routable.hasPath(path))
        .map(Routable::getMethod)
        .distinct()
        .collect(Collectors.joining(", "));
  }

  static List<String> getAllPaths() {
    return routables.stream()
        .map(Routable::getPath)
        .distinct()
        .collect(Collectors.toList());
  }

  class MissingResource extends RuntimeException {
    MissingResource(String resourcePath, Throwable cause) {
      super(resourcePath, cause);
    }
  }
}
