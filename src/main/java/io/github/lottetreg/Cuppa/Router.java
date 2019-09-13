package io.github.lottetreg.Cuppa;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Router {
  private List<Routable> routes;

  Router(List<Routable> routes) {
    this.routes = routes;
  }

  Response route(HTTPRequest request) {
    String requestPath = request.getPath();

    if (noRoutesMatchPath(requestPath)) {
      throw new NoMatchingPath(requestPath);
    }

    String requestMethod = request.getMethod();

    switch (requestMethod) {
      case "HEAD":
        return new Response(200);

      case "OPTIONS":
        return new Response(200, Map.of("Allow", getAllowedMethods(requestPath)));

      default:
        return routesWithMatchingPath(requestPath)
            .filter(route -> route.hasMethod(requestMethod))
            .findFirst()
            .orElseThrow(() -> new NoMatchingMethodForPath(requestMethod, requestPath))
            .getResponse(request);
    }
  }

  String getAllowedMethods(String path) {
    Stream<String> allowedMethods = this.routes.stream()
        .filter(route -> route.hasPath(path))
        .map(Routable::getMethod);

    return Stream.concat(allowedMethods, Stream.of("HEAD", "OPTIONS"))
        .distinct()
        .collect(Collectors.joining(", "));
  }

  private Stream<Routable> routesWithMatchingPath(String path) {
    return this.routes.stream()
        .filter(route -> route.hasPath(path));
  }

  private Boolean noRoutesMatchPath(String path) {
    return this.routes.stream()
        .noneMatch(route -> route.hasPath(path));
  }

  static class NoMatchingPath extends RuntimeException {
    NoMatchingPath(String path) {
      super(path);
    }
  }

  static class NoMatchingMethodForPath extends RuntimeException {
    NoMatchingMethodForPath(String method, String path) {
      super(method + " " + path);
    }
  }
}
